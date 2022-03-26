package com.example.italiando.other;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.italiando.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean jobNotEnqueued(WorkManager wm) {

        // Get a ListenableFuture with all jobs tagged 'CheckJob' (Async)
        ListenableFuture<List<WorkInfo>> jobs = wm.getWorkInfosByTag("CheckJob");

        try {
            // Jobs list
            List<WorkInfo> jobInfoList = jobs.get();

            // No job was scheduled
            if (jobInfoList.isEmpty()) return true;

            // Searching jobs with an ENQUEUED state
            for (WorkInfo jobInfo : jobInfoList) {

                // Saving job state
                WorkInfo.State state = jobInfo.getState();
                // Check
                if (state == WorkInfo.State.ENQUEUED) return false;
            }

            // No enqueued job was found
            return true;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void cancelJobs(WorkManager wm) {

        // Cancel all tagged jobs (Async)
        Operation info = wm.cancelAllWorkByTag("CheckJob");
        // cancelAllWorkByTag status
        ListenableFuture result = info.getResult();

        try {
            // cancelAllWorkByTag result
            Log.d("UTIL: cancel res", String.valueOf(result.get()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static String formatDate(long time){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public static Map<String,String> getMathQuestions(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("1+1","2");
        questions.put("2+2","4");
        questions.put("3+3","6");
        questions.put("4+4","8");
        questions.put("5+5","10");
        questions.put("6+6","12");
        questions.put("7+7","14");
        questions.put("8+8","16");
        questions.put("9+9","18");
        questions.put("10+10","20");
        questions.put("11+11","22");
        questions.put("12+12","24");
        questions.put("13+13","26");
        questions.put("14+14","28");
        questions.put("15+15","30");

        return questions;
    }

    public static Map<String,String> getRandomMathQuestions(int SIZE){
        HashMap<String,String> questionsMap = new HashMap<>();
        Map<String,String> originalQuestion = getMathQuestions();
        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }


    public static Map<String,Map<String,Boolean>> getIntroQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Stati Uniti",false);
        answer1.put("Cina",false);
        answer1.put("Italia ",true);
        answer1.put("Inghilterra",false);
        questions.put("Select Italy",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Stati Uniti",true);
        answer2.put("Italia",false);
        answer2.put("Inghilterra",false);
        answer2.put("Cina",false);
        questions.put("Select England",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Stati Uniti",false);
        answer3.put("Cina",true);
        answer3.put("Inghilterra",false);
        answer3.put("Italia",false);
        questions.put("Select China",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Spagna",false);
        answer4.put("Stati Uniti",false);
        answer4.put("Italia",false);
        answer4.put("Inghilterra",true);
        questions.put("Select England",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Io sono inglese.",false);
        answer5.put("Io sono italiano.",true);
        answer5.put("Tu sei italiano.",false);
        answer5.put("Io sono americano.",false);
        questions.put("I'm Italian.",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("She is american.",false);
        answer6.put("He is american.",true);
        answer6.put("I'm american.",false);
        answer6.put("You are american.",false);
        questions.put("Lui è americano.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("Io sono inglese.",true);
        answer7.put("Tu sei inglese.",false);
        answer7.put("Io sono spagnolo.",false);
        answer7.put("Lui è spagnolo.",false);
        questions.put("I'm english.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("Io vivo in America.",true);
        answer8.put("Io vivo in Spagna.",false);
        answer8.put("Io vivo in Inghilterra.",false);
        answer8.put("Tu vivi in America.",false);
        questions.put("I live in America.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("Italia",true);
        answer9.put("Inghilterra",false);
        answer9.put("America",false);
        answer9.put("Cina",false);
        questions.put("In ______ si mangia la pasta e la pizza",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("Inghilterra, inglese",true);
        answer10.put("Inglese, Inghilterra",false);
        answer10.put("Spagna, cinese",false);
        answer10.put("Cina, spagnolo",false);
        questions.put("In ______ si parla _____",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("spagnolo.",true);
        answer11.put("cinese.",false);
        answer11.put("italiano.",false);
        answer11.put("inglese",false);
        questions.put("In Messico e in spagna si parla ______ ",answer11);

        HashMap<String,Boolean> answer12 = new HashMap<>();
        answer12.put("I like italian people.",true);
        answer12.put("Italian people like me.",false);
        answer12.put("I like American people.",false);
        answer12.put("I like chinese people.",false);
        questions.put("A me piacciono gli italiani.",answer12);

        HashMap<String,Boolean> answer13 = new HashMap<>();
        answer13.put("Italia",true);
        answer13.put("Cina",false);
        answer13.put("Spagna",false);
        answer13.put("Stati Uniti",false);
        questions.put("Roma e Venezia  si trovano in _____",answer13);

        HashMap<String,Boolean> answer14 = new HashMap<>();
        answer14.put("Cina",true);
        answer14.put("America",false);
        answer14.put("Italia",false);
        answer14.put("Inghilterra",false);
        questions.put("A me piace la _____",answer14);

        HashMap<String,Boolean> answer15 = new HashMap<>();
        answer15.put("spagnoli",true);
        answer15.put("cinesi",false);
        answer15.put("Italia",false);
        answer15.put("Inghilterra",false);
        questions.put("A loro piacciono gli _____.",answer15);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getFoodsQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Apple",true);
        answer1.put("Pear",false);
        answer1.put("Kiwi",false);
        answer1.put("Pineapple",false);
        questions.put("Mela",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Pera",true);
        answer2.put("Mela",false);
        answer2.put("Kiwi",false);
        answer2.put("Ananas",false);
        questions.put("pear",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("ananas",true);
        answer3.put("mozzarella",false);
        answer3.put("patatine",false);
        answer3.put("salame piccante",false);
        questions.put("La pizza con l'_______, non è una vera pizza",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("salame piccante",true);
        answer4.put("ananas",false);
        answer4.put("patatine",false);
        answer4.put("melone",false);
        questions.put("La diavola è una pizza con il ______ e a volte le olive nere",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Pesce e patatine",true);
        answer5.put("Pesce e carrote",false);
        answer5.put("Carne e patatine",false);
        answer5.put("Carne e verdure",false);
        questions.put("Fish and chips",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("Acqua",true);
        answer6.put("Birra",false);
        answer6.put("Vino",false);
        answer6.put("Succo",false);
        questions.put("Water",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("Birra",true);
        answer7.put("Vino",false);
        answer7.put("Acqua",false);
        answer7.put("Succo",false);
        questions.put("Beer",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("Fragole",true);
        answer8.put("Ciliege",false);
        answer8.put("Fragola",false);
        answer8.put("Ciliegia",false);
        questions.put("Stroberries",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("Torta",true);
        answer9.put("Carne",false);
        answer9.put("Pomodoro",false);
        answer9.put("Cornetto",false);
        questions.put("Cake",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("cappuccino e cornetto",true);
        answer10.put("uova e salsiccia",false);
        answer10.put("tè e biscotti",false);
        answer10.put("torta e succo",false);
        questions.put("La tipica colazione italiana è _____ _ ______.",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Succo",true);
        answer11.put("Vino",false);
        answer11.put("Acqua",false);
        answer11.put("Birra",false);
        questions.put("Juice",answer11);

        HashMap<String,Boolean> answer12 = new HashMap<>();
        answer12.put("Un formaggio",true);
        answer12.put("Una carne",false);
        answer12.put("Un pesce",false);
        answer12.put("Una verdura",false);
        questions.put("La mozzarella è?",answer12);

        HashMap<String,Boolean> answer13 = new HashMap<>();
        answer13.put("Salad",true);
        answer13.put("T-bone steak",false);
        answer13.put("domplings",false);
        answer13.put("apple",false);
        questions.put("Insalata",answer13);

        HashMap<String,Boolean> answer14 = new HashMap<>();
        answer14.put("Melone",true);
        answer14.put("Pesca",false);
        answer14.put("Cocomero",false);
        answer14.put("Cocco",false);
        questions.put("Melon",answer14);

        HashMap<String,Boolean> answer15 = new HashMap<>();
        answer15.put("pizza",true);
        answer15.put("lasagna",false);
        answer15.put("tagliatella",false);
        answer15.put("parmigiana",false);
        questions.put("La ____ è un piatto originario di Napoli",answer15);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getNumbersQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("due",false);
        answer1.put("tre",false);
        answer1.put("uno",true);
        answer1.put("quattro",false);
        questions.put("1",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("due",true);
        answer2.put("tre",false);
        answer2.put("quattro",false);
        answer2.put("uno",false);
        questions.put("2",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("quattro",false);
        answer3.put("tre",true);
        answer3.put("cinque",false);
        answer3.put("sei",false);
        questions.put("3",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("sei",false);
        answer4.put("cinque",false);
        answer4.put("tre",false);
        answer4.put("quattro",true);
        questions.put("4",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("sei",false);
        answer5.put("cinque",true);
        answer5.put("sette",false);
        answer5.put("otto",false);
        questions.put("5",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("nove",false);
        answer6.put("dieci",true);
        answer6.put("otto",false);
        answer6.put("sette",false);
        questions.put("10",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("uno",true);
        answer7.put("quattro",false);
        answer7.put("sei",false);
        answer7.put("due",false);
        questions.put("tre, due, ___, via!",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("I eat a apple",true);
        answer8.put("I eat two apple",false);
        answer8.put("I drive a car",false);
        answer8.put("I read a book",false);
        questions.put("mangio una mela.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("tre",true);
        answer9.put("cinque",false);
        answer9.put("due",false);
        answer9.put("quattro",false);
        questions.put("uno, due, ___, stella!",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("due, tre, dieci",true);
        answer10.put("due, uno, otto",false);
        answer10.put("due, uno, dieci",false);
        answer10.put("nove, due, uno",false);
        questions.put("I have TWO apples, THREE pears and TEN cherries",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("uno, due, tre, quattro, cinque",true);
        answer11.put("uno, tre, cinque, sei",false);
        answer11.put("tre, uno, due",false);
        answer11.put("cinque, sei, sette, otto, tre",false);
        questions.put("which is the right order?",answer11);

        HashMap<String,Boolean> answer12 = new HashMap<>();
        answer12.put("un",true);
        answer12.put("una",false);
        answer12.put("due",false);
        answer12.put("la",false);
        questions.put("mangio ___ gelato",answer12);

        HashMap<String,Boolean> answer13 = new HashMap<>();
        answer13.put("due, quattro, sei, otto, dieci",true);
        answer13.put("quattro, otto, sei, due, dieci",false);
        answer13.put("due, tre, sei, otto, dieci",false);
        answer13.put("due, quattro, otto, sei, dieci",false);
        questions.put("uno, ____, tre, _____, cinque, _____, sette, _____, nove, ______",answer13);

        HashMap<String,Boolean> answer14 = new HashMap<>();
        answer14.put("quattro",true);
        answer14.put("tre",false);
        answer14.put("due",false);
        answer14.put("cinque",false);
        questions.put("2 + 2 = __",answer14);

        HashMap<String,Boolean> answer15 = new HashMap<>();
        answer15.put("quattro zero quattro",true);
        answer15.put("tre uno tre",false);
        answer15.put("tre zero tre",false);
        answer15.put("quatttro uno quattro",false);
        questions.put("____ error, page not found",answer15);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getAnimalsQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Leone",false);
        answer1.put("Cane",false);
        answer1.put("Toro",true);
        answer1.put("Gatto",false);
        questions.put("Bull",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("toro",true);
        answer2.put("cane",false);
        answer2.put("gatto",false);
        answer2.put("coniglio",false);
        questions.put("Il ___ ha due corna.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Animale domestico",true);
        answer3.put("Animale randagio",false);
        answer3.put("Animale in gabbia",false);
        answer3.put("Animale",false);
        questions.put("Pet",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Animal",true);
        answer4.put("Pet",false);
        answer4.put("Caged animal",false);
        answer4.put("Stray animal",true);
        questions.put("Animale",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Cavallo",true);
        answer5.put("Orso",false);
        answer5.put("Cane",false);
        answer5.put("Gatto",false);
        questions.put("Horse",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("cane",true);
        answer6.put("gatto",false);
        answer6.put("coniglio",false);
        answer6.put("topo",false);
        questions.put("Il _____ è il migliore amico dell'uomo",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("Mouse",true);
        answer7.put("Cat",false);
        answer7.put("Dog",false);
        answer7.put("Rabbit",false);
        questions.put("Topo",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("cavallo",true);
        answer8.put("cane",false);
        answer8.put("orso",false);
        answer8.put("gatto",false);
        questions.put("Mi piace andare a ____ e cavalcare in mezzo alla natura",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("orso",true);
        answer9.put("oca",false);
        answer9.put("anatra",false);
        answer9.put("agnello",false);
        questions.put("L'____ è un animale feroce, eppure è il pelushe più comune",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("Agnello",true);
        answer10.put("Coniglio",false);
        answer10.put("Maiale",false);
        answer10.put("Mucca",false);
        questions.put("Lamb",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Mucca",true);
        answer11.put("Maiale",false);
        answer11.put("Coniglio",false);
        answer11.put("Pesce",false);
        questions.put("Cow",answer11);

        HashMap<String,Boolean> answer12 = new HashMap<>();
        answer12.put("uccelli",true);
        answer12.put("mucche",false);
        answer12.put("elefanti",false);
        answer12.put("maiali",false);
        questions.put("Gli ____ sono volatili",answer12);

        HashMap<String,Boolean> answer13 = new HashMap<>();
        answer13.put("mucca",true);
        answer13.put("coniglio",false);
        answer13.put("maiale",false);
        answer13.put("tacchino",false);
        questions.put("La ____ ha la carne rossa",answer13);

        HashMap<String,Boolean> answer14 = new HashMap<>();
        answer14.put("tacchino",true);
        answer14.put("pollo",false);
        answer14.put("maiale",false);
        answer14.put("agnello",false);
        questions.put("per la festa del ringraziamento gli americani cucinano il _____",answer14);

        HashMap<String,Boolean> answer15 = new HashMap<>();
        answer15.put("canguro",true);
        answer15.put("cane",false);
        answer15.put("gatto",false);
        answer15.put("maiale",false);
        questions.put("Il ____ è l'animale simbolo dell'Australia",answer15);

        return questions;
    }



    public static Map<String,Map<String,Boolean>> getRandomLiteratureAndGeographyQuestions(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;
        if (subject.equals(context.getString(R.string.numbers))){
            originalQuestion = getNumbersQuestions();
        }else if (subject.equals(context.getString(R.string.Intro))){
            originalQuestion = getIntroQuestions();
        }else if (subject.equals(context.getString(R.string.animals))){
            originalQuestion = getAnimalsQuestions();
        }else{
            originalQuestion = getFoodsQuestions();
        }

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }

}
