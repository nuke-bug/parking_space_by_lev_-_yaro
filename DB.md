tables:

parking_spaces:
"CREATE TABLE if not exists 'parking_spaces' (" +  
        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +  
        "'type_of_place' text," +  //тип места
        "'number' INT," +  // Номер места
        "'busyness' text," +  // занято или свободно (занято/свободно)
        "'cost' INT" +  // стоимость в час        
        ");");
#todo yaro Яромир, чекни, может "cars" нам не нужно? 
cars: 
"CREATE TABLE if not exists 'cars' (" +  
        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +  
        "'owner' text," +  //владелец (ФИО)
        "'car_number' text," +  // Номер машины
        "'car_brand' text" +  // Марка     
        ");");

history:
"CREATE TABLE if not exists 'history' (" +  
        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +  
        "'owner' text," +  //владелец (фио)
        "'number' INT," +  // Номер места
        "'car_number' text," +  // Номер машины
        "'car_brand' text," +  // Марка     
        "'сheck_in_time' text," +  // время заезда
        "'departure_time' text," +  // время выезда ("не покидал территорию" по умолчанию)
        "'payment' text" +  // оплата(0 по умолчанию)
    
        ");");
        
get_history() //сортировка по времени и фио
find_in_history(car_number=None) //поиск по номеру машины
get_parking_spaces() // просмотр текущего состояния парковки
get_history_this_day() // кол-во выездов за последние сутки, суммарная прибыль
get_free_places() // вытаскиваем только не занятые места
post_new_client(owner, number, car_number, car_brand, сheck_in_time) //добавляет новую запись в  history, изменяет запись в parking_spaces на "занято".
post_old_client(car_number, сheck_in_time, departure_time, payment) // обновляет соответствующую запись в history,  изменяет запись в parking_spaces на "не занято".
get_cost_and_time возвращает время и цену места

- [x] оплата добавить в историю
- [x] в выезде добавить проверку
- [x] вывод суммы за день в статистике
- [x] вывод суммы за всё время в статистике

