# ExceptionReport [![](https://jitpack.io/v/cnrture/ExceptionReport.svg)](https://jitpack.io/#cnrture/ExceptionReport)

## Implementation
```kotlin
allprojects { 
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.cnrture:ExceptionReport:1.9'
}
```

## Usage
### Default
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExceptionReport(this, R.color.teal_700) // Color is optional
    }
}
```
### Custom Activity
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExceptionReport(this, R.color.teal_700) // Color is optional
            .setCustomActivity(CustomExceptionActivity::class.java)
    }
}
```

| Default | Custom Activity |
| ------- | -------------------- |
|<img src="https://github.com/cnrture/ExceptionReport/assets/29903779/ffd3946d-7897-4fe2-8396-afcdea342c4a"/>|<img src="https://github.com/cnrture/ExceptionReport/assets/29903779/f8bdb90f-7bf8-439a-9ca9-11476908406d"/>|

## License

```
Copyright 2023 Caner Türe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
