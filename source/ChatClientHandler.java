import java.net.*;
import java.io.*;
import java.util.*;
public class ChatClientHandler extends Thread{
    private Socket socket; //クライアントを表すソケット
    private BufferedReader in;
    private BufferedWriter out;
    private List clients;
    String name;
    
    public ChatClientHandler(Socket socket, List clients){
        this.socket = socket;
        this.clients = clients;
        this.name = "undefined"+(clients.size()+1);
    }
    
    public String getClientName(){
        return name;
    }
    
    public void run(){
        try{
            open();
            send("you joined chatserver as "+getClientName()); //クライアントが最初に接続した時に
            String returnMessage=getClientName()+": connected.";
            send(returnMessage);
            System.out.println(returnMessage); //接続したユーザーを表示
            while(true){
                String message = receive(getClientName());
                String[] commands = message.split(" ");
                if(commands[0].equalsIgnoreCase("post")){ //postコマンドの実行
                    post(commands[1]);
                } else if(commands[0].equals("bye")){ //byeコマンドの実行
                    bye();
                    break; //スレッドを終了する
                } else if(commands[0].equals("name")){ //nameコマンドの実行
                    name(commands[1]);
                } else if(commands[0].equals("whoami")){ //whoamiコマンドの実行
                    whoami();
                } else if(commands[0].equals("help")){ //helpコマンドの実行
                    help();
                } else if(commands[0].equals("users")){ //usersコマンドの実行
                    users();
                } else if(commands[0].equals("tell")){ //tellコマンドの実行
                    tell(commands[1], commands[2]);
                } else { //コマンド以外の入力の時
                    returnMessage=message+": unknown command";
                    send(returnMessage);
                    System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally{
            close();
        }
    }
    
    //tellコマンドによって指定された相手にのみメッセージを投稿する
    public void tell(String opponent, String message) throws IOException{
        
    }
    
    //byeコマンドによってチャットから退出する
    public void bye() throws IOException{
      String returnMessage="bye "+this.name; //退出時のメッセージ
      send(returnMessage);
      System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
      clients.remove(this); //リストから自身を削除する
    }
    
    //nameコマンドによってコマンド実行者のクライアントの名前を変更する
    public void name(String newName) throws IOException{
        
    }
    
    //whoamiコマンドによって現在設定されている名前を返す
    public void whoami() throws IOException{
        
    }
    
    //usersコマンドによって現在チャットに参加しているメンバの名前をクライアントに返す
    public void users() throws IOException{
        
    }
    
    //helpコマンドによって処理可能な命令の一覧を表示する
    public void help() throws IOException{
        
    }
    
    //postコマンドによってメッセージを投稿する
    public void post(String message) throws IOException{
        
    }
    
    
    //クライアントとのデータのやり取りを行うストリームを開くメソッド
    public void open() throws IOException{
        InputStream socketIn = socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socketIn));
        OutputStream socketOut = socket.getOutputStream();
        out = new BufferedWriter(new OutputStreamWriter(socketOut));  }
    
    //クライアントからデータを受け取るメソッド
    public String receive(String name) throws IOException{
        String line = in.readLine();
        System.out.print(name+": "+line);
        return line;
    }
    
    //クライアントにデータを送信するメソッド
    public void send(String message) throws IOException{
        //System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
        out.write(message);
        out.write("\r\n"); //改行
        //out.write("> ");//完全に動作しない
        out.flush();
    }
    
    //クライアントとの接続を閉じるメソッド
    public void close(){ 
        if(in != null){ try{ in.close();} catch(IOException e){}}
        if(out != null){ try{ out.close();} catch(IOException e){}}
        if(socket != null){ try{ socket.close();} catch(IOException e){}}
    }
}