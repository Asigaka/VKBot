package ru.asigaka.vkbot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartBotClass {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();

        Keyboard keyboard = new Keyboard();
        Keyboard keyboardGroups = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<List<KeyboardButton>> allKeyGroups = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        List<KeyboardButton> lineGroups = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Выбрать группу").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        lineGroups.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Т19ПКС/2").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        lineGroups.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Т-20/4").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        lineGroups.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("T19 РЗА/ЭС").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        lineGroups.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Т19ПКС/1").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line1);
        allKeyGroups.add(lineGroups);
        keyboard.setButtons(allKey);
        keyboardGroups.setButtons(allKeyGroups);

        GroupActor actor = new GroupActor(204477655,
                "033b69b6acbd05bc874245f07aad874608547c66f52bb21036d42d960e34dd2dc404ab4b1536ec1418775");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();

        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if (message.getText().equals("Начать")) {
                            vk.messages().send(actor).message("Привет!").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        }
                        if (message.getText().equals("Выбрать группу")) {
                            vk.messages().send(actor).message("Выберите группу").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboardGroups).execute();
                        }
                    } catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
