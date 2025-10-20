# 🧩 Spring Boot Task Manager API

Aplikacja REST API w **Kotlinie (Spring Boot)** do zarządzania użytkownikami, zadaniami, komentarzami i plikami.  
Używa **JWT** do autoryzacji i **MariaDB** do przechowywania danych.

## 🔧 Funkcjonalności
- Rejestracja i logowanie użytkowników (z hashowaniem haseł)
- CRUD dla:
  - 🧍 Użytkowników  
  - ✅ Zadań  
  - 💬 Komentarzy  
  - 📎 Plików powiązanych z zadaniami  
- Automatyczne pobieranie losowych użytkowników z API [randomuser.me](https://randomuser.me)
- Weryfikacja haseł i emaili
- Harmonogram zadań (Spring Scheduling)

## 💻 Technologie
Kotlin · Spring Boot · Exposed ORM · MariaDB · JWT · Gradle · Jackson
