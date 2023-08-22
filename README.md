<div align="center">
  <h1 align="center">
    <img src="logo.svg" width="150" alt="VK ID SDK Logo">
  </h1>
  <p align="center">
    VK ID SDK — библиотека для авторизации пользователей Android приложений с помощью аккаунта VK ID.
  </p>
</div>

## Предварительно

Общий план интеграции и в целом что такое VK ID можно прочитать здесь https://platform.vk.com/docs/vkid/1.35.0/plan

Для подключения VK ID SDK сначала необходимо получить ID приложения (app_id) и защищенный ключ (client_secret). Для их получения нужно создать приложение на [платформе для разработчиков](https://platform.vk.com/docs/vkid/1.35.0/create-application).


## Установка

Для начала работы добавьте репозиторий:
```kotlin
// todo настоящий репозиторий, пока его нет
maven {
    url("https://vkid-maven-public/")
}
```

Подключите библиотеку:
```kotlin
implementation("com.vk.id:vkid:${sdkVersion}")
```

И пропишите плейсхолдеры манифеста в секции `android` в `build.gradle.kts`:
```kotlin
android {
    //...
    addManifestPlaceholders(mapOf(
        "VKIDRedirectHost" to "vk.com", // обычно vk.com
        "VKIDRedirectScheme" to "vk1233445", // обычно vk{ID приложения}
    ))
}
```

## Интеграция
### Инициализация VK ID SDK
Вся работа происходит через объект `VKID`.
```kotlin
val vkid = VKID {
    context = this
    clientId = "1233445" // ID вашего приложения (app_id)
    clientSecret = "000000000000" // ваш защищенный ключ (client_secret)
    redirectUri = "vk1233445://vk.com" // ваш redirect URI, должно совпадать с тем что указано в VKIDRedirectScheme и VKIDRedirectHost в плейсхолдерах манифеста
}
```
### Авторизация
Результат авторизации передается в колбэк `VKID.AuthCallback`, поэтому нужно его объявить:
```kotlin
private val vkAuthCallback = object : VKID.AuthCallback {
    override fun onSuccess(accessToken: AccessToken) {     
        val token = accessToken.token
        //...
    }
 
    override fun onFail(fail: VKIDAuthFail) {
        when (fail) {
            is VKIDAuthFail.Canceled -> { /*...*/ }
            else -> {
                //...
            }
        }
    }
}
```
Сама авторизация запускается методом authorize, у которого есть два варианта вызова, suspend:
```kotlin
viewModelScope.launch {
    vkid.authorize(vkAuthCallback)
}
```
либо с передачей LifecycleOwner:
```kotlin
vkid.authorize(this@MainActivity, vkAuthCallback) // первый параметр LifecycleOwner, например активити
```

## Демонстрация

SDK поставляется с примером приложения, где можно посмотреть работу авторизации.
В папке [sample](sample) содержится тестовое приложение.

## Документация

- [Что такое VK ID](https://platform.vk.com/docs/vkid/1.35.0/start-page)
- [Создание приложения](https://platform.vk.com/docs/vkid/1.35.0/create-application)
- [Требования к дизайну](https://platform.vk.com/docs/vkid/1.35.0/guidelines/design-rules)

## Contributing
Разработка VK ID SDK происходит в открытом доступе на GitHub, и мы благодарны пользователям за внесение исправлений ошибок и улучшений. Читайте ниже, чтобы узнать, как вы можете принять участие в улучшении VK ID SDK.

### Code of Conduct
Пожалуйста, прочтите [нормы поведения](CODE_OF_CONDUCT.md), которые мы ожидаем от участников проекта. Он поможет понять, какие действия необходимы и какие недопустимы.

### Contributing Guide
Прочтите наше [руководство](CONTRIBUTING.md) чтобы узнать о нашем процессе разработки, как предлагать исправления и улучшения, и как добавлять и тестировать свои изменения в VK ID SDK. Так же рекомендуем ознакомится с [code style](CODE_STYLE.md) проекта.