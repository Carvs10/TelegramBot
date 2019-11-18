import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class Main {


    public static void main(String[] args) {



        // Creating bot object with access information

        TelegramBot bot = TelegramBotAdapter.build(" 894235389:AAGcUq7YbtAJZBVF8X00ZurKexLLUR3q-2U ");

        //Object that get the messages

        GetUpdatesResponse updatesResponse;

        //Object for send messages

        SendResponse sendResponse;

        //Manager for actions on the chat

        BaseResponse baseResponse;

        int messages = 0;

        // infinity loop that can be changed by a time set

        while (true){

            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(messages));

            // Message List

            List<Update> updates = updatesResponse.updates();

            //Analisys of every action on the message

            for(Update update : updates)
            {
                messages = update.updateId()+1;

                System.out.println("Recebendo menssagem:" + update.message().text());

                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                System.out.println("Resposta do chat action envida?"+ baseResponse.isOk());

                sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Nao entendi..."));

                System.out.println("Mensagem enviada?"+ sendResponse.isOk());
            }
        }
    }
}
