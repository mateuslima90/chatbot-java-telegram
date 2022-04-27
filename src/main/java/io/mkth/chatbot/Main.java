package io.mkth.chatbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void processingMessages(TelegramBot bot, Long chatId, Message message) {
        if (greeting(message)) {
            logger.info("Init chat with ".concat(message.from().firstName()));
            sendResponseToUser(bot, chatId, "Olá ".concat(message.from().firstName()));
            sendResponseToUser(bot, chatId, showAllOptions());
            sendResponseToUser(bot, chatId, "Um exemplo: somar 10 15");
        } else if(message.text().toLowerCase().startsWith("somar")) {
            var operation = message.text().split(" ");
            logger.info("Operação de Soma");
            try {
                var result = Integer.valueOf(operation[1]) + Integer.valueOf(operation[2]);
                logger.info("Primeiro número " + operation[1]);
                logger.info("Segundo número " + operation[2]);
                logger.info("Resultado " + result);
                sendResponseToUser(bot, chatId, "A soma é ".concat(String.valueOf(result)));
            } catch (Exception e) {
                logger.info("Soma exception: " + e);
                sendResponseToUser(bot, chatId, e.getMessage());
            }
        } else if(message.text().toLowerCase().startsWith("subtrair")) {
            logger.info("Operação de Subtração");
            var operation = message.text().split(" ");
            try {
                var result = Integer.valueOf(operation[1]) - Integer.valueOf(operation[2]);
                logger.info("Primeiro número " + operation[1]);
                logger.info("Segundo número " + operation[2]);
                logger.info("Resultado " + result);
                sendResponseToUser(bot, chatId, "A subtração é ".concat(String.valueOf(result)));
            } catch (Exception e) {
                logger.info("Subtração exception: " + e);
                sendResponseToUser(bot, chatId, e.getMessage());
            }
        } else if(message.text().toLowerCase().startsWith("dividir")) {
            logger.info("Operação de Dividir");
            var operation = message.text().split(" ");
            try {
                var result = Integer.valueOf(operation[1]) / Integer.valueOf(operation[2]);
                logger.info("Primeiro número " + operation[1]);
                logger.info("Segundo número " + operation[2]);
                logger.info("Resultado " + result);
                sendResponseToUser(bot, chatId, "A divisão é ".concat(String.valueOf(result)));
            } catch (Exception e) {
                logger.info("Divisão exception: " + e);
                sendResponseToUser(bot, chatId, "Não é possível fazer divisão por 0");
            }
        }
        else if(message.text().toLowerCase().startsWith("multiplicar")) {
            logger.info("Operação de Multiplicação");
            var operation = message.text().split(" ");
            try {
                var result = Integer.valueOf(operation[1]) * Integer.valueOf(operation[2]);
                logger.info("Primeiro número " + operation[1]);
                logger.info("Segundo número " + operation[2]);
                logger.info("Resultado " + result);
                sendResponseToUser(bot, chatId, "A multiplicação é ".concat(String.valueOf(result)));
            } catch (Exception e) {
                logger.info("Multiplicação exception: " + e);
                sendResponseToUser(bot, chatId, e.getMessage());
            }
        } else {
            logger.info("Mensagem enviada " + message.text());
            sendResponseToUser(bot, chatId, "Não entendi, pode repetir, por favor");
        }

    }

    public static boolean validateOptions(Message message) {
        if(message.text().startsWith("1") || message.text().startsWith("2") ||
                message.text().startsWith("3") || message.text().startsWith("4")) {
            return true;
        }

        return false;
    }

    public static String showAllOptions() {
        String options = "Eu consigo te ajudar nas operações abaixo" +
                "\n1 - Somar" +
                "\n2 - Subtrair" +
                "\n3 - Dividir" +
                "\n4 - Multiplicar";
        return options;
    }

    public static boolean greeting(Message message) {
        List<String> greetingList = Arrays.asList("olá", "ola", "oi", "opa");
        if(greetingList.contains(message.text().toLowerCase())){
            return true;
        }
        return false;
    }

    public static void sendResponseToUser(TelegramBot bot, Long chatId, String message) {
        //envio da mensagem de resposta
        var sendResponse = bot.execute(new SendMessage(chatId, message));
        //verificação de mensagem enviada com sucesso
        System.out.println("Mensagem Enviada?" +sendResponse.isOk());
    }

    public static void main(String[] args) {

        for(int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }

        TelegramBot bot = new TelegramBot(args[0]);

        GetUpdatesResponse updatesResponse;

        int m=0;

        while(true) {
            //executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
            updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));

            //lista de mensagens
            List<Update> updates = updatesResponse.updates();

            //análise de cada ação da mensagem
            for (Update update : updates) {
                //atualização do off-set
                m = update.updateId()+1;
                processingMessages(bot, update.message().chat().id(), update.message());
            }
        }
    }
}
