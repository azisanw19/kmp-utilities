![badge][badge-android]
![badge][badge-ios]

# KMP Utilities

KMP Library Utilities is a Kotlin Multiplatform library that offers utility functions to 
simplify cross-platform development. It is designed to boost productivity with ready-to-use 
features across multiple target platforms.

## Documentation

For more information, check the documentation:

- [Date Time Utilities](/docs/FeinnDateTime.md)
- [Document uri Launcher Utilities](/docs/FeinnLauncher.md)
- [Utilities Permission](/docs/FeinnPermission.md)
- [Platform Context](/docs/FeinnPlatformContext.md)
- [Byte Buffer](/docs/FeinnByteBuffer.md)
- [Cryptographic](/docs/FeinnCrypto.md)
- [Screenshot]() **Cooming Soon**

## Example

Coming soon...

### Android

The Example Android App can be built and installed via [Android Studio], or via command line by 
executing:

```shell
./gradlew installDebug
```

### iOS

The iOS project is generated via:

```shell
./gradlew generateXcodeProject
```

> [!TIP]
> `./gradlew openXcode` can be used to both generate the project _and_ open it in Xcode.

In Xcode, configure signing, then run.

## Contributing

We welcome contributions to improve this project! Please follow these steps to ensure a smooth 
collaboration:

1. **Open an Issue**

Before starting any work, kindly open an issue to discuss your proposed changes or additions. 
This helps us align on the direction and avoid duplicate efforts.

2. **Fork the Repository**

Once your issue has been reviewed and approved, fork the repository to your GitHub account. 
This allows you to make changes independently.

3. **Create a New Branch**

Create a new branch based on the main branch in your forked repository.
Use a descriptive name for your branch, such as `feature/add-new-component` or `bugfix/fix-login-error`
or `dev/#issue-number`.

```bash
git checkout main
git checkout -b feature/your-branch-name
```

4. **Submit a Pull Request**

Once your work is complete, push your changes to your forked repository and open a pull request (PR)
to merge your branch into the main branch of the original repository. Include a clear description
of the changes you’ve made and reference the related issue in your PR.

> [!NOTE]
> Ensure your code adheres to the project’s coding standards and includes relevant tests or 
> documentation updates.

5. **Collaborate on Review**

Be ready to respond to feedback or requested changes during the review process. This ensures your

Thank you for contributing and helping us improve this project! 😊

## Find this library useful?
Support it by joining __[stargazers](https://github.com/azisanw19/kmp-utilities/stargazers)__ for this repository.
Also, __[follow me](https://github.com/azisanw19)__ on GitHub for more libraries! 🤩

You can always <a href="https://buymeacoffee.com/azisanw19"><img src="https://img.buymeacoffee.com/button-api/?text=Buy%20me%20a%20coffee&emoji=&slug=azisanw19&button_colour=FFDD00&font_colour=000000&font_family=Cookie&outline_colour=000000&coffee_colour=ffffff"></a>

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```







[Android Studio]: https://developer.android.com/studio

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat
[badge-linux]: http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat
[badge-windows]: http://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/platform-macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/platform-tvos-808080.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/platform-wasm-624FE8.svg?style=flat