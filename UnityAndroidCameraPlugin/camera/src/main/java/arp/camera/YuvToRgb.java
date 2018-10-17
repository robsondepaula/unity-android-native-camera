/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package arp.camera;

import android.graphics.ImageFormat;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.util.Size;
import android.view.Surface;


public class YuvToRgb implements Allocation.OnBufferAvailableListener {
    private Allocation mInputAllocation;
    private Allocation mOutputAllocation;
    private Allocation mOutputAllocationInt;
    private Size mInputSize;
    private ScriptC_yuv2rgb mScriptYuv2Rgb;

    private int[] mOutBufferInt;
    private long mLastProcessed;

    private final int mFrameEveryMs;


    YuvToRgb(RenderScript rs, Size dimensions, int frameMs) {
        mInputSize = dimensions;
        mFrameEveryMs = frameMs;

        createAllocations(rs);

        mInputAllocation.setOnBufferAvailableListener(this);

        mScriptYuv2Rgb = new ScriptC_yuv2rgb(rs);
        mScriptYuv2Rgb.set_gCurrentFrame(mInputAllocation);
        mScriptYuv2Rgb.set_gIntFrame(mOutputAllocationInt);
    }

    private void createAllocations(RenderScript rs) {

        final int width = mInputSize.getWidth();
        final int height = mInputSize.getHeight();

        mOutBufferInt = new int[width * height];

        Type.Builder yuvTypeBuilder = new Type.Builder(rs, Element.YUV(rs));
        yuvTypeBuilder.setX(width);
        yuvTypeBuilder.setY(height);
        yuvTypeBuilder.setYuvFormat(ImageFormat.YUV_420_888);
        mInputAllocation = Allocation.createTyped(rs, yuvTypeBuilder.create(),
                Allocation.USAGE_IO_INPUT | Allocation.USAGE_SCRIPT);

        Type rgbType = Type.createXY(rs, Element.RGBA_8888(rs), width, height);
        Type intType = Type.createXY(rs, Element.U32(rs), width, height);

        mOutputAllocation = Allocation.createTyped(rs, rgbType,
                Allocation.USAGE_IO_OUTPUT | Allocation.USAGE_SCRIPT);
        mOutputAllocationInt = Allocation.createTyped(rs, intType,
                Allocation.USAGE_SCRIPT);
    }

    Surface getInputSurface() {
        return mInputAllocation.getSurface();
    }

    void setOutputSurface(Surface output) {
        mOutputAllocation.setSurface(output);
    }

    @Override
    public void onBufferAvailable(Allocation a) {
        // Get the new frame into the input allocation
        mInputAllocation.ioReceive();

        final long current = System.currentTimeMillis();
        if ((current - mLastProcessed) >= mFrameEveryMs) {
            mScriptYuv2Rgb.forEach_yuv2rgbFrames(mOutputAllocation);

            // update output buffer
            mOutputAllocationInt.copyTo(mOutBufferInt);

            mLastProcessed = current;
        }
    }

    public int[] getOutputBuffer() {
        return mOutBufferInt;
    }

}