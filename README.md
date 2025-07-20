# ExceptionReport [![](https://jitpack.io/v/cnrture/ExceptionReport.svg)](https://jitpack.io/#cnrture/ExceptionReport)

> A powerful and user-friendly Android library for handling uncaught exceptions with advanced
> debugging features and elegant UI.

## 🚀 Features

- **🎯 Code Highlighting**: Automatically highlights the exact line of code that caused the exception
- **🔍 Smart Stack Trace Analysis**: Distinguishes between user code and system code for faster
  debugging
- **🎨 Modern UI**: Beautiful Material Design 3 interface with intuitive user experience
- **📋 Easy Sharing**: Copy to clipboard or share exception reports with one tap
- **⚙️ Customizable**: Support for custom activities
- **📱 Lightweight**: Minimal impact on app performance and size

## 📦 Installation

Add the JitPack repository to your root `build.gradle`:

```gradle
allprojects { 
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency to your module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.cnrture:ExceptionReport:1.9'
}
```

## 📖 Usage

### Basic Implementation

Initialize ExceptionReport in your main activity:

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExceptionReport(this)
    }
}
```

### Advanced Configuration
#### Custom Exception Activity

```kotlin
ExceptionReport(this).setCustomActivity(CustomExceptionActivity::class.java)
```

## 🎨 Screenshots

<div align="center">

|                                                         Default                                                          |                                                     Custom Activity                                                      |
|:------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/cnrture/ExceptionReport/assets/29903779/ffd3946d-7897-4fe2-8396-afcdea342c4a" width="250"/> | <img src="https://github.com/cnrture/ExceptionReport/assets/29903779/f8bdb90f-7bf8-439a-9ca9-11476908406d" width="250"/> |

</div>

## 🛠️ Key Benefits

### For Developers

- **Faster Debugging**: Instantly identify problematic code with visual highlighting
- **Better Error Context**: Clear distinction between your code and framework code
- **Professional Reporting**: Clean, organized exception reports for better communication

### For Users

- **Graceful Error Handling**: No more sudden app crashes without explanation
- **Easy Reporting**: Simple interface to report issues back to developers
- **Minimal Disruption**: Quick recovery options to continue using the app

## 📋 Requirements
- **Minimum SDK**: API 21 (Android 5.0)
- **Compile SDK**: API 35
- **Language**: Kotlin

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open
an issue first to discuss what you would like to change.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

```
Copyright 2025 Caner Türe

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

---

<div align="center">
<strong>⭐ If this library helped you, please give it a star! ⭐</strong>
</div>
