cd ..
javac -d "./storage/generator" -encoding UTF-8 -classpath "./storage/generator/*;./storage/generator;."  .\resources\generator\definitions\types\* .\resources\generator\templates\*
java -classpath "./storage/generator/*;./storage/generator;./resources/generator;." com.kamicloud.generator.Generator
php artisan stub-api:sync-services
pause
