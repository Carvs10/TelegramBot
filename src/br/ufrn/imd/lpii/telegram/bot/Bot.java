package br.ufrn.imd.lpii.telegram.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



class Bot {
    private static TelegramBot bot;
    @SuppressWarnings("unused")
    private Localizacao localizacao;

    /**
     * Construtor da classe.
     *
     * Declara um objeto da classe bot para a utilização das funções da API do telegram
     *
     * @param token Token criado e coletado no BotFather que faz parte da API do telegram, que serve para criar e comunicar-se com o bot
     *
     *
     */
    // Criação do objeto bot
    Bot(String token){ Bot.bot = TelegramBotAdapter.build(token); }

    /**
     *  A função não recebe nenhum parâmetro, ela serve simplesmente para iniciar o loop da coleta de mensagens para
     *  assim iniciar as funconalidades do código
     *
     *
     */
    void inicarBot(){
        // objeto responsável por receber as mensagens
        GetUpdatesResponse updatesResponse;

        // objeto responsável por gerenciar o envio de respostas
        SendResponse sendResponse;

        // objeto responsável por gerenciar o envio de ações do chat
        BaseResponse baseResponse;

        // controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
        int limiteInicial = 0;

        // loop infinito pode ser alterado por algum timer de intervalo curto
        while (true) {
            Long idChat = null;
            int escolherConstrucao = 0;

            try {
                new Thread();
                Thread.sleep(500);

                // executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
                updatesResponse = bot.execute(new GetUpdates().limit(100).offset(limiteInicial));

                // lista de mensagens
                List<Update> updates = updatesResponse.updates();

                // análise de cada ação da mensagem
                if (updates != null) {
                    for (Update update : updates) {

                        // atualização do off-set
                        limiteInicial = update.updateId() + 1;

                        System.out.println("Recebendo mensagem:" + update.message().text());

                        switch (update.message().text()) {
                            case "/cadastrarbem":
                                escolherConstrucao = 1;
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira os itens:\n" +
                                        "    Nome\n    Descrição\n    Código\n    Localização\n    Categoria\nSepardos por ';'\nExemplo: nome"
                                        + ";descricao;codigo;localizacao;categoria"));
                                cadastro(update.message().chat().id(), limiteInicial, escolherConstrucao);
                                limiteInicial = update.updateId()+1;
                                break;

                            case "/cadastrarlocalizacao":
                                escolherConstrucao = 2;
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira os itens:\n" +
                                        "    Nome\n    Descrição\nSepardos por ';'\nExemplo: nome"
                                        + ";descricao"));
                                cadastro(update.message().chat().id(), limiteInicial, escolherConstrucao);
                                limiteInicial = update.updateId()+1;
                                break;

                            case "/cadastrarcategoria":
                                escolherConstrucao = 3;
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira os itens:\n" +
                                        "    Nome\n    Descrição\n    Código\nSepardos por ';'\nExemplo: nome"
                                        + ";descricao;codigo"));
                                cadastro(update.message().chat().id(), limiteInicial, escolherConstrucao);
                                limiteInicial = update.updateId()+1;
                                break;

                            case "/listarlocalizacoes":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Listando as localizações cadastras..."));
                                listarLocalizacao(update.message().chat().id());
                                limiteInicial = update.updateId()+1;
                                break;
                            case "/listarcategorias":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Listando as categorias cadastras..."));
                                listarCategoria(update.message().chat().id());
                                limiteInicial = update.updateId()+1;
                                break;
                            case "/listarbensdeumalocalizacao":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira a localização"));
                                listarBensDeUmaLocalizacao(update.message().chat().id(), limiteInicial);
                                limiteInicial = update.updateId()+1;
                                break;
                            case "/buscarbempornome":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome do bem a ser buscado"));
                                buscarBem(update.message().chat().id(), limiteInicial);
                                break;
                            case "/buscarbemporcodigo":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira o código do bem a ser buscado"));
                                buscarBem(update.message().chat().id(), limiteInicial);
                                break;
                            case "/buscarbempordescricao":
                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                                bot.execute(new SendMessage(update.message().chat().id(), "Insira a descrição do bem a ser buscado"));
                                buscarBem(update.message().chat().id(), limiteInicial);
                                break;
//                            case "/movimentarbem":
//                                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
//                                bot.execute(new SendMessage(update.message().chat().id(), "Insira a código do bem ao qual será alterada a localização"));
//                                movimentarBem(update.message().chat().id(), limiteInicial);
//                                break;
                        }

                        // envio de "Escrevendo" antes de enviar a resposta
                        baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                        // verificação de ação de chat foi enviada com sucesso
                        System.out.println("Resposta de Chat Action Enviada:" + baseResponse.isOk());

                        // envio da mensagem de resposta
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Escolha um comando"));

                        // verificação de mensagem enviada com sucesso
                        System.out.println("Mensagem Enviada:" + sendResponse.isOk());

                        idChat = update.message().chat().id();
                    }
                }
            } catch (InterruptedException e) {
                bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                bot.execute(new SendMessage(idChat, "Chat encerrado devido a grande período de inatividade"));
                e.printStackTrace();
            }
        }
    }


    private void cadastro(Long idChat, int limiteInicial, int escolherConstrucao) {
        // Variável que repsenta a string a ser coletada pelo usuário
        String mensagem;
        boolean controle = true;
        while(controle) {

            // Objeto que recebe as mensagens do usuário
            GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(limiteInicial));

            // Lista das mensagens
            List<Update> mensagensUsuario = updatesResponse.updates();

            if(mensagensUsuario != null) {
                for(Update update : mensagensUsuario) {
                    limiteInicial = update.updateId()+1;
                    mensagem = update.message().text();
                    // Função responsável por receber a entrada do usuário e trata-lá
                    tratarMensagem(mensagem, idChat, escolherConstrucao);
                    controle = false;
                    break;
                }
            }
        }
    }


    private void tratarMensagem(String entradaDoUsuário, Long idChat, int escolherConstrucao) {
        String[] r = entradaDoUsuário.split(";");

        bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
        bot.execute(new SendMessage(idChat, "Cadastrando"));

        if(escolherConstrucao == 1) {
            Bem bem = new Bem(r[0], r[1], r[2], r[3], r[4]);
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Registrando bem no banco de dados"));

            String nomeDoArquivo = "ListaDeBens.json";
            armazenarEmArquivoJson(bem, nomeDoArquivo);

            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Bem registrado no banco de dados!"));
        }
        else if(escolherConstrucao == 2) {
            Localizacao localizacao = new Localizacao(r[0], r[1]);
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Registrando localização no banco de dados"));

            String nomeDoArquivo = "ListaDeLocalizacoes.json";
            armazenarEmArquivoJson(localizacao, nomeDoArquivo);
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Localização registrada no banco de dados!"));
        }
        else if (escolherConstrucao == 3){
            Categoria categoria = new Categoria(r[0], r[1], r[2]);
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Registrando categoria no banco de dados"));

            String nomeDoArquivo = "ListaDeCategorias.json";
            armazenarEmArquivoJson(categoria, nomeDoArquivo);
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Categoria registrada no banco de dados!"));
        }
        bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
        bot.execute(new SendMessage(idChat, "Cadastro concluído com sucesso!"));
    }

    private <T> void armazenarEmArquivoJson(T item, String nomeDoArquivo) {
        // Criação do Json com "GsonBuilder" para que ele fique disposto de forma mais legível no arquivo json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // ArrayList de objetos onde será carregado os objetos do arquivo Json
        ArrayList<T> patrimonio = new ArrayList<T>();

        // Objeto para verificação da existencia do arquivo
        File file = new File(nomeDoArquivo);

        // Verifica se o arquivo a ser aberto já existe
        if (file.exists()) {
            try (Reader reader = new FileReader(nomeDoArquivo)) {
                // Variável para leitura do arquivo Json
                JsonReader jsonReader = new JsonReader(reader);

                // Lê o arquivo Json e grava as informações no ArrayList
                patrimonio = gson.fromJson(jsonReader, new TypeToken<ArrayList<T>>() {}.getType());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }


        // Sobreescreve o arquivo Json anterior com o novo patrimônio adicionado
        try(FileWriter arquivoJson = new FileWriter(nomeDoArquivo)){
            // Adiciona novo item ao ArrayList
            patrimonio.add(item);
            // ArrayList para arquivo
            gson.toJson(patrimonio, arquivoJson);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listarLocalizacao(Long idChat){
        String arquivo = "ListaDeLocalizacoes.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Localizacao> localizacoes;

        try (Reader reader = new FileReader(arquivo)) {
            // Variável para leitura do arquivo Json
            JsonReader jsonReader = new JsonReader(reader);

            // Lê o arquivo Json e grava as informações no ArrayList
            localizacoes = gson.fromJson(jsonReader, new TypeToken<ArrayList<Localizacao>>() {}.getType());

            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            localizacoes.forEach(x -> bot.execute(new SendMessage(idChat, "Nome: " + x.getNome() + "\nDescrição: " + x.getDescricao())));

        }catch (FileNotFoundException e){
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Não há localizações cadastradas"));
        } catch (IOException e) {
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Ocorreu um erro, tente novamente."));
        }
    }

    private void listarCategoria(Long idChat){
        String arquivo = "ListaDeCategorias.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Categoria> categorias;

        try (Reader reader = new FileReader(arquivo)) {
            // Variável para leitura do arquivo Json
            JsonReader jsonReader = new JsonReader(reader);

            // Lê o arquivo Json e grava as informações no ArrayList
            categorias = gson.fromJson(jsonReader, new TypeToken<ArrayList<Categoria>>() {}.getType());

            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            categorias.forEach(x -> bot.execute(new SendMessage(idChat, "Nome: " + x.getNome() + "\nDescrição: " + x.getDescricao()
             + "\nCódigo: " + x.getCodigo())));

        }catch (FileNotFoundException e){
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Não há categorias cadastradas"));
        } catch (IOException e) {
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Ocorreu um erro, tente novamente."));
        }
    }

    private void listarBensDeUmaLocalizacao(Long idChat, int limiteInicial) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Bem> bens;

        // objeto responsável por receber as mensagens
        GetUpdatesResponse updatesResponse;

        // objeto responsável por gerenciar o envio de respostas
        SendResponse sendResponse;

        // objeto responsável por gerenciar o envio de ações do chat
        BaseResponse baseResponse;

        String entradaDoUsuario = null;
        boolean controle = true;

        while(controle){
            try{
                new Thread();
                Thread.sleep(500);

                // executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
                updatesResponse = bot.execute(new GetUpdates().limit(100).offset(limiteInicial));

                // lista de mensagens
                List<Update> updates = updatesResponse.updates();

                if(updates != null){
                    for (Update update : updates){
                        entradaDoUsuario = update.message().text();
                        controle = false;
                        break;
                    }
                }
            } catch (InterruptedException e){
                bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                bot.execute(new SendMessage(idChat, "Chat encerrado devido a inatividade"));
            }
        }

        try (Reader reader = new FileReader("ListaDeBens.json")) {
            // Variável para leitura do arquivo Json
            JsonReader jsonReader = new JsonReader(reader);

            // Lê o arquivo Json e grava as informações no ArrayList
            bens = gson.fromJson(jsonReader, new TypeToken<ArrayList<Bem>>() {}.getType());

            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Os seguintes bens se localizam em: " + entradaDoUsuario));

            for (Bem bem : bens) {
                if (entradaDoUsuario.equals(bem.getLocalizacao())) {
                    bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                    bot.execute(new SendMessage(idChat, "Nome: " + bem.getNome() + "\nDescrição: " + bem.getDescricao()
                            + "\nCódigo: " + bem.getCodigo() + "\nLocalização: " + bem.getLocalizacao() + "\nCategoria: " + bem.getCategoria()));
                }
            }

        } catch (FileNotFoundException e){
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Não há bens cadastradas"));
        } catch (IOException e) {
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Ocorreu um erro, tente novamente."));
        }
    }

    private Bem buscarBem(Long idChat, int limiteInicial){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Bem> bens;

        // objeto responsável por receber as mensagens
        GetUpdatesResponse updatesResponse;

        // objeto responsável por gerenciar o envio de respostas
        SendResponse sendResponse;

        // objeto responsável por gerenciar o envio de ações do chat
        BaseResponse baseResponse;

        String entradaDoUsuario = "";
        boolean controle = true;

        while(controle){
            try{
                System.out.println("Chegou no While do buscarBem");
                // executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
                updatesResponse = bot.execute(new GetUpdates().limit(100).offset(limiteInicial));

                // lista de mensagens
                List<Update> updates = updatesResponse.updates();

                if(updates != null){
                    for (Update update : updates){
                        limiteInicial = update.updateId()+1;
                        entradaDoUsuario = update.message().text();
                        controle = false;
                        updates.clear();
                        break;
                    }
                }
            } catch (RuntimeException e){
                bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                bot.execute(new SendMessage(idChat, "Chat encerrado devido a inatividade"));
                e.printStackTrace();
            }
        }

        try (Reader reader = new FileReader("ListaDeBens.json")) {
            // Variável para leitura do arquivo Json
            JsonReader jsonReader = new JsonReader(reader);

            // Lê o arquivo Json e grava as informações no ArrayList
            bens = gson.fromJson(jsonReader, new TypeToken<ArrayList<Bem>>() {}.getType());

            for (Bem bem : bens) {
                if (entradaDoUsuario.equals(bem.getNome())){
                    bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                    bot.execute(new SendMessage(idChat, "Nome: " + bem.getNome() + "\nDescrição: " + bem.getDescricao()
                            + "\nCódigo: " + bem.getCodigo() + "\nLocalização: " + bem.getLocalizacao() + "\nCategoria: " + bem.getCategoria()));
                    return bem;
                }
                else if (entradaDoUsuario.equals(bem.getDescricao())){
                    bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                    bot.execute(new SendMessage(idChat, "Nome: " + bem.getNome() + "\nDescrição: " + bem.getDescricao()
                            + "\nCódigo: " + bem.getCodigo() + "\nLocalização: " + bem.getLocalizacao() + "\nCategoria: " + bem.getCategoria()));
                    return bem;
                }
                else if (entradaDoUsuario.equals(bem.getCodigo())) {
                    bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
                    bot.execute(new SendMessage(idChat, "Localização do bem: " + bem.getLocalizacao()));
                    return bem;
                }
            }

            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Não achei :/"));

        }catch (FileNotFoundException e){
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Não há bens cadastradas"));
        } catch (IOException e) {
            bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
            bot.execute(new SendMessage(idChat, "Ocorreu um erro, tente novamente."));
        }
        Bem bem = new Bem();
        return bem;
    }

//    private void movimentarBem(Long idChat, int limiteInicial){
//
//        Bem bem = buscarBem(idChat, limiteInicial);
//
//        // objeto responsável por receber as mensagens
//        GetUpdatesResponse updatesResponse;
//
//        // objeto responsável por gerenciar o envio de respostas
//        SendResponse sendResponse;
//
//        // objeto responsável por gerenciar o envio de ações do chat
//        BaseResponse baseResponse;
//
//        String entradaDoUsuario = "";
//        boolean controle = true;
//
//        bot.execute(new SendMessage(idChat, "Insira a nova localização do bem"));
//        while(controle){
//            System.out.println("Entrou no While de movimentar bens");
//            try{
//                // executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
//                updatesResponse = bot.execute(new GetUpdates().limit(100).offset(limiteInicial));
//
//                // lista de mensagens
//                List<Update> updates = updatesResponse.updates();
//                if(updates != null){
//                    for (Update update : updates){
//                        limiteInicial = update.updateId()+1;
//                        entradaDoUsuario = update.message().text();
//                        controle = false;
//                        continue;
//                    }
//                }
//            } catch (RuntimeException e){
//                bot.execute(new SendChatAction(idChat, ChatAction.typing.name()));
//                bot.execute(new SendMessage(idChat, "Chat encerrado devido a inatividade"));
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("Saiu do while de movimentar");
//
//        System.out.println(entradaDoUsuario);
//
//        bem.setLocalizacao(entradaDoUsuario);
//
//        armazenarEmArquivoJson(bem, "ListaDeBens.json");
//
//        bot.execute(new SendMessage(idChat, "Movimentação feita com sucesso"));
//    }
}
