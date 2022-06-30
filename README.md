# Client-server-calendar-with-Java-Swing
Projekt kalendarza wykonany na zaliczenie przedmiotu na studiach. 
Chcę go dalej rozwijać - poprawić jakość kodu, wzbogacić o nowe technologie (np. Spring i połączenie z bazą danych)

Uruchamiając program sprawdzane jest czy jest uruchomiony serwer oraz zależnie od tego czy zapisaliśmy nazwę użytkownika czy nie 
wyświetlone jest okno logowania lub od razu główne okno programu. Podczas logowania podajemy dowolny ciąg znaków i zależnie od tego 
czy taki był już podany pobierane są wydarzenia tego użytkownika lub jest tworzony nowy z podaną nazwą.

Po uruchomieniu się głównego okna widzimy menu, listę wydarzeń, zegar, panel z dzisiejszymi imieninami oraz nietypowym świętem – jest 
to też zakładka „Lista wydarzeń”. W zakładce „Kalendarz” możemy zmieniać miesiące za pomocą strzałek na UI lub po prostu na klawiaturze. 
Dodać nowe wydarzenie można po kliknięciu w dany dzień. Wyświetlane jest wtedy okno „Nowe wydarzenie”, gdzie możemy wybrać dodanie wydarzenia 
trwającego wybrany okres czasu lub wydarzenie całodniowe. Zapisujemy przyciskiem zapisz lub kombinacją klawiszy CTRL + S. Po zapisaniu 
wyświetla się lista wydarzeń z dodanym już wydarzeniem. 

Zamykając program przyciskiem „Wyloguj” zostaje usunięty lokalny plik użytkownika z zapamiętaną nazwą i przy następnym otwarciu 
programu trzeba się zalogować, a zamykając program normalnie (iksem) plik zostaje (jeśli był zapamiętany) i przy następnym otwarciu 
programu użytkownik nie musi się logować. 
