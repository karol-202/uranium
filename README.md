# Uranium

![Deployment](
https://github.com/karol-202/uranium/workflows/Deployment/badge.svg
)
[ ![Download](https://api.bintray.com/packages/karol202/uranium/uranium-core/images/download.svg) ](
https://bintray.com/karol202/uranium/uranium-core/_latestVersion
)
[ ![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg) ](
https://opensource.org/licenses/MIT
)

Uranium is an **universal, declarative UI library** for Kotlin Multiplatform inspired by architecture of React.js.
It allows you to create applications for **various platforms** using beautiful **DSL** in component-based manner.

## Contents
- [How to install?](#how-to-install)
- [Why uranium?](#why-uranium)
- [How does it work?](#how-does-it-work)
- [Contributing](#contributing)

## How to install?

This project acts only as a platform-agnostic core of the uranium library,
and you probably don't want to use it directly (unless contributing to uranium)
as it does not provide any useful platform implementation.

Instead, you can use one of the below uranium libraries:
- [uranium-swing](https://github.com/karol-202/uranium-swing) -
uranium implementation allowing creating cross-platform (run in JVM) Swing-based desktop apps
- [uranium-arkade](https://github.com/karol-202/uranium-arkade/) -
uranium implementation for creating 2D games, currently supports HTML5 canvas
- more implementations are planned

However, if you would like to use uranium-core directly,
for example in order to create your own platform implementation,
you can gather it from [jCenter](https://bintray.com/karol202/uranium/uranium-core).

Gradle (Kotlin DSL):
```kotlin
dependencies {
    implementation("pl.karol202.uranium.core:uranium-core-jvm:0.2.5")
}
```

Gradle (Groovy):
```groovy
dependencies {
    implementation "pl.karol202.uranium.core:uranium-core-jvm:0.2.5"
}
```

Maven:
```xml
<dependency>
    <groupId>pl.karol202.uranium.core</groupId>
    <artifactId>uranium-core-jvm</artifactId>
    <version>0.2.5</version>
</dependency>
```

Make sure you have jCenter in your `repositories` section:
```groovy
repositories {
    jcenter()
}
```

uranium-core comes in a few variants:
- `uranium-core` - common artifact,
useful when using in Kotlin Multiplatform project
- `uranium-core-jvm` - JVM artifact
- `uranium-core-js` - JS (browser) artifact
- `uranium-core-wasm32` - Kotlin/Native WebAssembly artifact

Support for more Kotlin/Native platforms is planned, feel free to create an issue in case of need.

## Why uranium?

React is undoubtedly a nice way to create web or mobile applications.
It is liked for its architecture allowing one to create component-based apps with in simple way.
However, it has one big drawback - the language.
Javascript, in addition to many other flaws,
is not a language that fits well into platforms other than web,
because, due to its interpreted nature, it's simply slow
(take example of React Native or Electron,
that despite being an interesting cross-platform alternatives,
are noticeably less responsive than native apps).

There have been ports of React to Kotlin/JS for those wanting to
combine advantages of both the library and the language.
Although it resolved many of the JS language flaws related to writing app itself,
it was only a partial solution, because it was only designed for React for web pages.
Still, other downsides (such as the performance one) were making it
impossible or impractical to use it on mobile, desktop or to create high-performance games.

So why not to create a React-like library from scratch in Kotlin?
Kotlin can be compiled to many targets (JVM, JS, Windows, Linux,
macOS, WASM, etc.), so as a language it fits perfectly for such a library to be written in.
In addition, it is type safe, concise and interoperable with other languages.
That's why I created uranium.

uranium aims to combine the advantages of React, such as being declarative,
component-based, unidirectional and simple with qualities of Kotlin, while
being fully universal and possible to use on every platform without performance issues.

## How does it work?

Every piece of layout in Uranium is a component.
Components have their own lifecycle managed internally by uranium.
The only thing necessary to create component is a `render` method,
that you can use to build complex hierarchies using other components.

```kotlin
class HelloComponent(props: BasicProps) : SwingAbstractAppComponent<BasicProps>(props)
{
    override fun SwingRenderScope.render() = boxLayout(axis = BoxAxis.Y) {
        + label().text("Hello")
        + label().text("world")
    }
}
```

Every component receives props as input from parent component.
Additionally, you can define state if you want your component to be stateful.
State changes automatically make affected components rerender,
so there's no need for updating everything manually in the imperative way.

```kotlin
class NameComponent(props: Props) : SwingAbstractAppComponent<NameComponent.Props>(props),
                                    UStateful<NameComponent.State>
{
    data class Props(override val key: Any,
                     val initialName: String) : UProps

    data class State(val name: String) : UState

    override var state by state(State(props.initialName))

    override fun SwingRenderScope.render() = boxLayout(axis = BoxAxis.Y) {
        + textField()
                .text(state.name)
                .onTextChange(::setName)
        + label()
                .text("Hello, ${state.name}")
    }

    private fun setName(text: String) = setState { copy(name = text) }
}
```

Above is a simple Swing component containing a text field and a label displaying value from the text field.

For more details on how uranium works, see documentation.

## Contributing

Contributions are highly welcome.

If you find a bug or would like have some feature implemented, file an issue.
You can also create a pull request if you have working solution for some issue.
