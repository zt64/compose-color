# compose-color

🎨 Compose multiplatform color picker

## Styles

<details>
<summary>Color Circle</summary>

```kotlin
var color = remember { mutableStateOf(Color.Red) }

ColorCircle(
    color = color,
    onColorChange = { color = it }
)
```

</details>

<details>
<summary>Color Square</summary>

</details>

<details>
<summary>Color Ring</summary>

```kotlin
var color = remember { mutableStateOf(Color.Red) }

ColorRing(
    color = color,
    onColorChange = { color = it }
)
```

</details>

<details>
<summary>Color Well</summary>
* Apple color
  picker [https://developer.apple.com/documentation/appkit/nscolorwell](https://developer.apple.com/documentation/appkit/nscolorwell)

</details>

## Setup

To use the library, add the following to your `build.gradle.kts`:

```toml
[versions]
composeColor = "x.y.z"

[libraries]
composeColor = { module = "dev.zt64.compose-color:compose-color", version.ref = "composeColor" }
```

## License

[GPL v3.0](LICENSE)