### TrackFinderApp

ТЗ 1: Использую публичное апи itunes (https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/SearchExamples.html#//apple_ref/doc/uid/TP40017632-CH6-SW1)
 создать приложение по поиску треков. 
 Условия: один экран с отображением списка треков, поле ввода для поиска, ячейку списка сделать из cardview с elevation,
 в ячейке отображать: название трека автора альбома и обложку альбома, ответ кэшировать в базу данных.
 Обязательно: SingleActivity, Retrofit, Room, MVVM, разделить по слоям 
 data(repository - обращается к базе данных и к апи) 
 domain(interactor - обращается к repository)
 presentation(viewmodel - обращается к interactor, 
 fragment - подписывается на список из viewmodel), использовать flow во всех слоях для передачи,
 dagger (инъектить: api dao interactor repository), diffUtil, ViewBinding
