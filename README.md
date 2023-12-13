<div align="center">
  <h1 align="center">
    <img src="logo.svg" width="150" alt="VK ID SDK Logo">
  </h1>
  <p align="center">
    <a href="https://artifactory-external.vkpartner.ru/ui/native/vkid-sdk-andorid/com/vk/id/">
        <img src="https://img.shields.io/badge/stability-beta-red">
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/npm/l/@vkid/sdk?maxAge=3600">
    </a>
    <a href="https://artifactory-external.vkpartner.ru/ui/native/vkid-sdk-andorid/com/vk/id/">
        <img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory-external.vkpartner.ru%2Fartifactory%2Fvkid-sdk-andorid%2Fcom%2Fvk%2Fid%2Fvkid%2Fmaven-metadata.xml"/>
    </a>
  </p>
  <p align="center">
    VK ID SDK — библиотека для авторизации пользователей Android-приложений с помощью аккаунта VK ID.
  </p>
</div>

---
:information_source: VK ID SDK сейчас находится в бета-тестировании. О проблемах вы можете сообщить с помощью <a href="https://github.com/VKCOM/vkid-android-sdk/issues">issues репозитория</a>.

---

## Демонстрация

SDK поставляется с [тестовым примером приложения](sample/app), где можно посмотреть работу авторизации.
Чтобы тестовое приложение успешно собралось, сначала создайте файл `secrets.properties` в корне проекта и пропишите в нем client_id и client_secret вашего приложения VK ID:


Файл `secrets.properties`:
```
VKIDClientSecret=Ваш защищённый ключ
VKIDClientID=Ваш ID приложения
```

## Предварительно

Что такое VK ID и как интегрировать его в приложение читайте здесь https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan.

Чтобы подключить VK ID SDK, сначала получите ID приложения (app_id) и защищенный ключ (client_secret). Для этого создайте приложение в [кабинете подключения VK ID](https://id.vk.com/business/go).


## Установка

Для начала работы добавьте репозиторий:
```kotlin
maven {
    url("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-andorid/")
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
        "VKIDClientID" to "1233445", // ID вашего приложения (app_id).
        "VKIDClientSecret" to "000000000000", // Ваш защищенный ключ (client_secret).
        "VKIDRedirectHost" to "vk.com", // Обычно используется vk.com.
        "VKIDRedirectScheme" to "vk1233445", // Обычно используется vk{ID приложения}.
    ))
}
```

## Интеграция
### Инициализация VK ID SDK
Инициализируйте работу VK ID SDK через объект `VKID`.
```kotlin
val vkid = VKID(context)
```
### Авторизация
Результат авторизации передается в коллбэк `VKID.AuthCallback`, поэтому его нужно объявить:
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
Авторизация запускается с помощью метода authorize(), у которого есть два варианта вызова:
```kotlin
viewModelScope.launch {
    vkid.authorize(vkAuthCallback)
}
```
или с передачей LifecycleOwner:
```kotlin
vkid.authorize(this@MainActivity, vkAuthCallback) // Первый параметр LifecycleOwner, например активити.
```

## Документация

- [Что такое VK ID](https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/start-page)
- [Создание приложения](https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/connection/create-application)
- [Требования к дизайну](https://id.vk.com/business/go/docs/ru/vkid/archive/1.60/vk-id/guidelines/design-rules)


## Локальная сборка
Если проект не собирается со странными ошибками, то скореe всего нужно выставить в настройках проекта в студии jdk-17. Для того, чтобы работали градл-скрипты из консоли, также нужно прописать в переменной среды JAVA_HOME путь по которому находится jdk 17-й версии.

## Contributing
Проект VK ID SDK имеет открытый исходный код на GitHub, и вы можете присоединиться к его доработке — мы будем благодарны за внесение улучшений и исправление возможных ошибок.

### Contributing Guide
В [руководстве](CONTRIBUTING.md) вы можете подробно ознакомиться с процессом разработки и узнать, как предлагать улучшения и исправления, а ещё — как добавлять и тестировать свои изменения в VK ID SDK.
Также рекомендуем ознакомиться с общими [правилами оформления кода](CODE_STYLE.md) в проекте.
