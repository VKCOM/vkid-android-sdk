<div align="center">
  <h1 align="center">
    <img src="logo.svg" width="150" alt="VK ID SDK Logo">
  </h1>
  <p align="center">
    <a href="https://artifactory-external.vkpartner.ru/ui/native/vkid-sdk-android/com/vk/id/">
        <img src="https://img.shields.io/badge/stability-beta-red">
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/npm/l/@vkid/sdk?maxAge=3600">
    </a>
    <a href="https://artifactory-external.vkpartner.ru/ui/native/vkid-sdk-android/com/vk/id/">
        <img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory-external.vkpartner.ru%2Fartifactory%2Fvkid-sdk-android%2Fcom%2Fvk%2Fid%2Fvkid%2Fmaven-metadata.xml"/>
    </a>
  </p>
  <p align="center">
    VK ID SDK — библиотека для авторизации пользователей Android-приложений с помощью аккаунта VK ID.
  </p>
</div>

---

:information_source: Версия VK ID SDK 2.0.0-alpha поддерживает авторизацию по протоколу [OAuth 2.1](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-10), а также способы входа через аккаунты Одноклассников и Mail.ru. Если вы хотите участвовать в тестировании этой версии SDK или узнать о ней подробнее, напишите нам на почту devsupport@corp.vk.com.

Текущая стабильная версия VK ID SDK - [1.3.2](https://github.com/VKCOM/vkid-android-sdk/releases/tag/v1.3.2).

---

## Демонстрация

SDK поставляется с [тестовым примером приложения](sample/app), где можно посмотреть работу авторизации.
Чтобы тестовое приложение успешно собралось, сначала создайте файл `sample/app/secrets.properties` и пропишите в нем client_id и client_secret вашего приложения VK ID:


Файл `secrets.properties`:
```
VKIDClientSecret=Ваш защищённый ключ
VKIDClientID=Ваш ID приложения
```

## Предварительно

Что такое VK ID и как интегрировать его в приложение читайте [в статье "Начало работы"](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/android/install).

Чтобы подключить VK ID SDK, сначала получите ID приложения (app_id) и защищенный ключ (client_secret). Для этого создайте приложение в [кабинете подключения VK ID](https://id.vk.com/business/go).

Документацию по API SDK можно найти [на Github pages](https://vkcom.github.io/vkid-android-sdk/).


## Установка

Для начала работы добавьте репозиторий:
```kotlin
maven {
    url("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
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
    defeaultConfig {
        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to "1233445", // ID вашего приложения (app_id).
                "VKIDClientSecret" to "000000000000", // Ваш защищенный ключ (client_secret).
                "VKIDRedirectHost" to "vk.com", // Обычно используется vk.com.
                "VKIDRedirectScheme" to "vk1233445", // Обычно используется vk{ID приложения}.
            )
        )
    }
}
```

## Интеграция
### Инициализация VK ID SDK
Инициализируйте работу VK ID SDK через объект `VKID`.
```kotlin
// В Application
fun onCreate() {
    super.onCreate()
    VKID.init(this)
}
```
### Авторизация
Результат авторизации передается в коллбэк `VKIDAuthCallback`, поэтому его нужно объявить:
```kotlin
private val vkAuthCallback = object : VKIDAuthCallback {
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

### Обновление токена
Токен живет ограниченное количество времени, при получении ошибки от апи обновите его:
```kotlin
viewModelScope.launch {
    VKID.instance.refreshToken(
        callback = object : VKIDRefreshTokenCallback {
            override fun onSuccess(token: AccessToken) {
                // Использование token
            }
            override fun onFail(fail: VKIDRefreshTokenFail) {
                when (fail) {
                    is FailedApiCall -> fail.description // Использование текста ошибки
                    is RefreshTokenExpired -> fail // Это означает, что нужно пройти авторизацию заново
                    is Unauthorized -> fail // Пользователь понимает, что сначала нужно авторизоваться
                }
            }
        }
    )
}
```

Также есть версия с передачей LifecycleOwner:
```kotlin
VKID.instance.refreshToken(
    lifecycleOwner = MainActivity@this,
    callback = ... // такой же, как в suspend версии
)
```

## Документация

- [Что такое VK ID](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/intro/start-page)
- [Создание приложения](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/create-application)
- [Требования к дизайну](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/guidelines/design-rules-oauth)


## Локальная сборка
Если проект не собирается со странными ошибками, то скореe всего нужно выставить в настройках проекта в студии **jdk-17**. Для того, чтобы работали градл-скрипты из консоли, также нужно прописать в переменной среды JAVA_HOME путь по которому находится jdk **17-й** версии.

## Contributing
Проект VK ID SDK имеет открытый исходный код на GitHub, и вы можете присоединиться к его доработке — мы будем благодарны за внесение улучшений и исправление возможных ошибок.

### Contributing Guide
В [руководстве](CONTRIBUTING.md) вы можете подробно ознакомиться с процессом разработки и узнать, как предлагать улучшения и исправления, а ещё — как добавлять и тестировать свои изменения в VK ID SDK.
Также рекомендуем ознакомиться с общими [правилами оформления кода](CODE_STYLE.md) в проекте.
