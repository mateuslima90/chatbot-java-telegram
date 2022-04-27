package io.mkth.chatbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void processingMessages(TelegramBot bot, Long chatId, Message message) {
        if (greeting(message)) {
            logger.info("Init chat with ".concat(message.from().firstName()));
            sendResponseToUser(bot, chatId, "Olá ".concat(message.from().firstName()));
        } else {
            logger.info("Mensagem enviada " + message.text());
            sendResponseToUser(bot, chatId, message.text());
        }
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
        SendResponse sendResponse = bot.execute(new SendMessage(chatId, message));
        //verificação de mensagem enviada com sucesso
        System.out.println("Mensagem Enviada?" +sendResponse.isOk());
    }

    public static void main(String[] args) {

        String token = "incluir o seu token aqui";

        TelegramBot bot = new TelegramBot(token);

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
