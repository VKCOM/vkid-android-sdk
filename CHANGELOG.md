# CHANGELOG

> **Note**\
> Описание основных изменений в релизах VK ID SDK. Наш SDK следует [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## 2.4.0 (14-05-2025)

### Added
+ Добавили поддержку Captcha. Теперь она обрабатывается в запросах, которые делает VK ID SDK, по умолчанию. Также упрощена интеграция обработки Captcha, если вы используете Access token для своих запросов в api.vk.com. Подробнее об этом читайте в [документации](!!ru/vkid/latest/vk-id/connection/start-integration/android/install.mdx#Podderzhka-Captcha).
## 2.3.2 (19-03-2025)

### Changed
+ Обновлён текст в виджете «Mail», когда он используется как единственный дополнительный способ входа. Теперь в кнопке авторизации с помощью Mail используется текст «Войти с Mail».
+ Локаль, которая позволяет настроить язык для текстов, теперь применяется к авторизации в WebView.

### Fixed
+ Ранее при отсутствии установленного Chrome Browser авторизация в приложении VK могла не открываться. Исправлено. Теперь авторизация работает корректно.

## 2.3.0-alpha02 (27.02.2025)

### Changed
- Понизили версию Compose bom до 2024.05.00

## 2.3.1 (19-02-2025)

### Added
+ Добавили возможность настроить язык для текстов UI-компонентов с помощью локали. Подробнее читайте в [документации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/start-integration/android/install). 
+ Добавили возможность автоподнятия шторки авторизации. Для интеграции воспользуйтесь [документацией](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/elements/onetap-drawer/floating-onetap-android#Avtopodnyatie-shtorki-Compose) 
+ Добавили сбор крашей и статистики с помощью AppTracer. Если вам нужно отключить эту функциональность, воспользуйтесь [документацией]( https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/start-integration/android/install). 

### Changed
+ Сделали небольшой редизайн в SDK: теперь шторка авторизации выглядит более акцентно, а прозрачные виджеты имеют белый фон. Также классы XXXStyle.TransparentYYY теперь deprecated, но продолжают работать. Пожалуйста, обновите стили, если вы их использовали (XXXStyle.TransparentYYY → XXXStyle.SecondaryYYY), чтобы соответствовать гайдлайнам по дизайну VK.

## 2.3.0-alpha (29-01-2025)

### Added
+ Запустили возможность предложить пользователю после авторизации в сервисе подписаться на сообщество ВКонтакте. С помощью настроек вы можете показать пользователю экран подписки на сообщество сразу после авторизации или в любой момент при использовании приложения. Пока функциональность доступна для нескольких партнёров.

## 2.2.2 (12.10.2024)

### Changed
Технический релиз — поработали над внутренними улучшениями SDK.

## Placeholders plugin 1.1.0 (08.11.2024)

### Fixed
- Исправлен способ подключения через явное указание всех свойств в property. Ранее они были помечены как internal, то есть внутренние.

## 2.2.1 (14.10.2024)

### Fixed
- Исправили анимацию нажатия на One Tap. Ранее нажатия на кнопку могли отображаться не очень чётко, поэтому доработана анимация Ripple Effect. Теперь касание пользователя видно более явно.

## 2.2.0 (27.08.2024)

### Changed
- Поддержали ребрендинг Почты Mail: в SDK обновлены все экраны с логотипом Почты, а также изменено название сервиса с Mail.ru на Mail. Доработки в коде из-за ребрендинга не требуются.

## Placeholders plugin 1.0.0 (27.08.2024)

### Added
- Добавлен плагин плейсхолдеров как альтернативный способ инициализации SDK. Он позволяет инциализировать плейсхолдеры в одном месте, если у вас несколько application-модулей. Подробнее читайте в [документации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/start-integration/android/install).

## 2.1.0 (07-08-2024)

### Added
- Добавлена возможность выбрать текст кнопки One Tap, который увидит пользователь. Это позволяет адаптировать кнопку для разных сценариев — например, для получения услуги отобразить текст «Записаться c VK ID» . Подробнее о настройке текста в кнопке читайте в [документации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/elements/onetap-button/onetap-android).

## 2.0.1 (16-07-2024)

### Added
- В параметр `data` коллбэка `onAuthCode` добавлено поле `deviceId` со значением, которое нужно передавать в запрос получения токена при генерации параметров PKCE на стороне Backend вашего сервиса. Подробнее читайте в [документации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/start-integration/auth-flow-android).

### Fixed
- Ранее кнопка One Tap могла сужаться в иконку прежде, чем заканчивалось свободное меcто. Также не работал `Modifier.fillMaxWidth()`. Исправлено. Теперь адаптивность кнопки работает корректно. 

## 2.0.0 (25-06-2024)

### Added
- [VK ID](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/intro/plan) теперь поддерживает авторизацию по протоколу [OAuth 2.1](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-10). За счет работы авторизации на передовом стандарте обеспечивается высокая защита пользовательских данных.
- Для пользователя добавлена возможность входа через аккаунты «Одноклассников» и Mail.ru. Для отображения кнопок входа через эти сервисы интегрируйте [виджет 3 в 1](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/intro/main#Vidzhet-3-v-1) — блок с кнопками будет располагаться на окне авторизации вашего сервиса — или подключите [дополнительные OAuth](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/intro/main#Podklyuchenie-dopolnitelnyh-OAuth) — для показа кнопок на окне авторизации VK ID.
- Добавлена возможность отключить быструю авторизацию c помощью параметра `fastAuthEnabled` в OneTap и OneTapBottomSheet. Подробнее читайте в статьях про кнопку [One Tap](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/elements/onetap-button/onetap-android#Otklyuchenie-bystroj-avtorizacii) и [шторку авторизации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/elements/onetap-drawer/floating-onetap-android#Otklyuchenie-bystroj-avtorizacii). 
- Поддержана совместная работа с vk-android-sdk «из коробки» в модуле vk-sdk-support. Подробнее читайте в [документации](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/start-integration/android/joint-use-with-vksdk).

### Breaking changes
- Произошли изменения в публичном интерфейсе, связанные с поддержкой авторизации с OAuth 2.1. Для перехода с SDK предыдущей версии и поддержки этих изменений воспользуйтесь [инструкцией](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/migration/android/migration-on-oauth-2.1).

### Fixed
- Исправлена опечатка в названии maven-репозитория. Часть url изменена с «andorid» на «android». Поддерживаются оба варианта.

## 2.0.0-alpha04

### Added
- Добавлена опция CONSENT в параметр авторизации Prompt. При передаче CONSENT всегда будет показываться экран подтверждения входа при авторизации без провайдера.

### Fixed
- Исправлен автоматический вход в аккаунт при нажатии на OneTap-кнопку без подтянутого пользователя. Теперь, если в кнопке нет пользователя, всегда показывается промежуточный экран, где можно подтвердить вход или сменить аккаунт.

## 1.3.3

### Fixed
Сделали важные исправления внутренней работы SDK. Если вы используете более раннюю версию, пожалуйста, обновитесь на версию 1.3.3

## 2.0.0-alpha03

### Added
- Добавлена возможность отключить быструю авторизацию параметром fastAuthEnabled в OneTap и OneTapBottomSheet.
  Подробности смотрите в статьях [Кнопка One Tap](https://id.vk.com/about/business/go/docs/en/vkid/latest/vk-id-2/connection/android/onetap) и [Шторка авторизации](https://id.vk.com/about/business/go/docs/en/vkid/latest/vk-id-2/connection/android/floating-onetap) в разделе "Отключение быстрой авторизации".

### Fixed
- Исправлен краш при авторизации с временным возвратом в клиент SDK

## 2.0.0-alpha02

### Added
- Возможность получения Refresh token с помощью проперти VKID#refreshToken
- Возможность получения полученных скопов для Access token с помощью проперти AccessToken#scopes
- Возможность смены порядка OAuth в виджете 3в1

### Changed
- Сделан приватным конструктор класса AccessToken

## 2.0.0-alpha
Альфа релиз для тестирования выборочными партнерами. 
В этой версии много изменений публичного интерфеса и он ломает обратную совместимость.
Миграция описана в [статье](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/android/migration-on-oauth-2.1)

### Added
- Поддержка OAuth2.1. [Статья с документацией](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/android/oauth-2.1-methods)
- Поддержка совместной работы с vk-android-sdk "из коробки" в модуле vk-sdk-support. [Статья с документацией](https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id-2/connection/android/migration-on-oauth-2.1#Sovmestnoe-ispolzovanie-s-VK-ANDROID-SDK)

### Fixed
- Исправлена опечатка в названии maven репозитория. Часть урла изменена с "andorid" на "android". Поддерживаются оба варианта
 
## 1.3.2

### Added
- В сeмпле теперь можно скопировать access token в буфер обмена
 
### Fixed
- Фикс дублирования класса UserId при подключении библиотек https://github.com/VKCOM/vk-android-sdk
 
### Changed
- Обновлена поддержка SSL Pinning

## 1.3.1

### Changed
- Обновлена поддержка SSL Pinning

## 1.3.0

### Added
- Документация классов SDK
- Поддержка системной темы во всех компонентах
- Поддержка Skippable функций в классах SDK
- Отображение данных пользователя в семпле

### Changed
- Elevation игнорируется для Transparent стилей OneTap

### Fixed
- Обрезание тени OneTap

## 1.2.0

### Added
- Возможность использовать системную тему в XML виджетах
- Улучшен перформанс с помощью baseline profile

### Changed
- Объект VKID запрещено менять в виджетах после отрисовки
- Список OneTapOAuth и объект VKID в OneTapBottomSheet теперь можно менять програмно
- Иконка и сплеш скрин с семпле

### Fixed
- Сжатие контента в OneTapBottomSheet
- Невлезание контента в OneTap
- Обрезание текста при выболе большого размера текста
- Отображение марджинов элементов в семпле