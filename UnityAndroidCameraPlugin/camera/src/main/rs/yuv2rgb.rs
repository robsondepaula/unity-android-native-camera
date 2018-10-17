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
#pragma version(1)
#pragma rs java_package_name(arp.camera)
#pragma rs_fp_relaxed

rs_allocation gCurrentFrame;
rs_allocation gIntFrame;

uchar4 __attribute__((kernel)) yuv2rgbFrames(uint32_t x, uint32_t y)
{
    uchar yuvY = rsGetElementAtYuv_uchar_Y(gCurrentFrame, x, y);
    uchar yuvU = rsGetElementAtYuv_uchar_U(gCurrentFrame, x, y);
    uchar yuvV = rsGetElementAtYuv_uchar_V(gCurrentFrame, x, y);

    uchar4 out = rsYuvToRGBA_uchar4(yuvY, yuvU, yuvV);

    // Since the rendering is performed in OpenGL we need this odd layout of ABGR
    uint32_t px = 0xff000000 | out.b << 16 | out.g << 8 | out.r;

    rsSetElementAt_int(gIntFrame, px, x, y);

    return out;
}

