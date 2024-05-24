# CHANGELOG

> **Note**\
> Описание основных изменений в релизах VK ID SDK. Наш SDK следует [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 2.0.0
Для этого релиза, такие же изменения, как в 2.0.0-alpha плюс несколько дополнительных

### Added
- Возможность получения Refresh token с помощью проперти VKID#refreshToken
- Возможность получения полученных скопов для Access token с помощью проперти AccessToken#scopes

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