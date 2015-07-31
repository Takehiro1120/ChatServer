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
    
  }
  
  public String getClientName(){
    
  }
  
  public void run(){
    
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
    
  }
  
  //クライアントからデータを受け取るメソッド
  public String receive(String name) throws IOException{ 
    
  }
  
  //クライアントにデータを送信するメソッド
  public void send(String message) throws IOException{ 
    
  }
  
  //クライアントとの接続を閉じるメソッド
  public void close(){ 
    
  }
}