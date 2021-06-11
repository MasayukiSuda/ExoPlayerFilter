# ExoPlayerFilter
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
<img src="https://img.shields.io/badge/license-MIT-green.svg?style=flat">
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)

This library uses OpenGL Shaders to apply effects on [ExoPlayer](https://github.com/google/ExoPlayer) video at Runtime and <br> depends EXOPlayer core 2.14.0.<br>
<img src="art/art.gif" width="33.33%">

## Gradle
Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```groovy
    dependencies {
        implementation 'com.github.MasayukiSuda:ExoPlayerFilter:v0.2.6'
        implementation 'com.google.android.exoplayer:exoplayer-core:2.14.0'
    }
```
This library depends ExoPlayer core 2.14.0

## Sample Usage

### STEP 1
Create [SimpleExoPlayer](https://google.github.io/ExoPlayer/guide.html#creating-the-player) instance. 
In this case, play MP4 file. <br>
Read [this](https://google.github.io/ExoPlayer/guide.html#add-exoplayer-as-a-dependency) if you want to play other video formats. <br>
```JAVA
    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"));

    // SimpleExoPlayer
    player = new SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(new ProgressiveMediaSource.Factory(dataSourceFactory))
            .build();
    player.addMediaItem(MediaItem.fromUri(Constant.STREAM_URL_MP4_VOD_SHORT));
    player.prepare();
    player.setPlayWhenReady(true);

```


### STEP 2
Create [EPlayerView](https://github.com/MasayukiSuda/ExpPlayerFilter/blob/master/epf/src/main/java/com/daasuu/epf/EPlayerView.java) and set SimpleExoPlayer to EPlayerView.

```JAVA
    ePlayerView = new EPlayerView(this);
    // set SimpleExoPlayer
    ePlayerView.setSimpleExoPlayer(player);
    ePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    // add ePlayerView to WrapperView
    ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(ePlayerView);
    ePlayerView.onResume();
```
### STEP 3
Set Filter. Filters is [here](https://github.com/MasayukiSuda/ExpPlayerFilter/tree/master/epf/src/main/java/com/daasuu/epf/filter).<br>
Custom filters can be created by inheriting [GlFilter.java](https://github.com/MasayukiSuda/ExpPlayerFilter/blob/master/epf/src/main/java/com/daasuu/epf/filter/GlFilter.java).
```JAVA
    ePlayerView.setGlFilter(new GlSepiaFilter());
```


## Special Thanks to
* [android-gpuimage](https://github.com/CyberAgent/android-gpuimage)


## License
Copyright 2017 MasayukiSuda

MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


#### ExoPlayer and ExoPlayer demo.

    Copyright (C) 2014 The Android Open Source Project
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
