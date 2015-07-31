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
        //相手の名前と一致するクライアントを探す
        for(int i=0; i<clients.size(); i++){ //クライアント数繰り返す
            ChatClientHandler handler = (ChatClientHandler)clients.get(i);
            if(handler.getClientName().equals(opponent)==true) { //相手の名前と一致した時
                handler.send("["+this.getClientName()+"->"+opponent+"] "+message);//メッセージを送る
                String returnMessage = opponent;//送った相手の名前を表示する
                send(returnMessage);
                System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
                return; //役目を終えたので終了
            }
        }
        String returnMessage=opponent+": unknown user"; //送信相手が見つからなかった時、その旨を表示
        send(returnMessage);
        System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
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
		//すべてのクライアントの名前を参照する
		for(int i=0; i<clients.size(); i++){
			ChatClientHandler handler = (ChatClientHandler)clients.get(i);
			//参照しているクライアントの名前とコマンド対象の名前が同じの時
			if(handler.getClientName().equals(newName)==true) {
				String returnMessage = "["+newName+"] is already used by other clients"; //エラーメッセージ
				send(returnMessage);
				System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
				return; //コマンド処理を終了する
			}
		}
		this.name=newName; //コマンドを実行したクライアントの名前を更新
		String returnMessage="change name to "+name; //新しい名前を表示
		send(returnMessage);
		System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
	}
	
    //whoamiコマンドによって現在設定されている名前を返す
    public void whoami() throws IOException{
        String returnMessage = getClientName(); //クライアントの名前を取得し表示する
        send(returnMessage);
        System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
    }
    
    //usersコマンドによって現在チャットに参加しているメンバの名前をクライアントに返す
    public void users() throws IOException{
        //クライアントを確認し名前を記憶する
        List names = new ArrayList(); //名前を記憶する配列
        for(int i=0; i<clients.size(); i++){ //クライアント数繰り返す
            ChatClientHandler handler = (ChatClientHandler)clients.get(i);
            names.add(handler.getClientName()); //対象のユーザーを記憶
        }
        Collections.sort(names); //記憶した名前を並び替える
        String returnMessage = ""; 
        for(int i=0; i<names.size(); i++){
            if(i>0) returnMessage=returnMessage+","; //最初のループ出ない時、","をメッセージに追加する
            returnMessage=returnMessage+names.get(i); //クライアントの名前をメッセージに追加する
        }
        this.send(returnMessage); //クライアントの一覧を表示する
    }
    
    //helpコマンドによって処理可能な命令の一覧を表示する
    public void help() throws IOException{
        String returnMessage = "bye, help, name, post, users, tell, whoami";
        send(returnMessage);
        System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
    }
    
    	//postコマンドによってメッセージを投稿する
	public void post(String message) throws IOException{
		//メッセージを自分以外のクライアントに送る
		List names = new ArrayList(); //名前を記憶する配列
		for(int i=0; i<clients.size(); i++){ //クライアント数繰り返す
			ChatClientHandler handler = (ChatClientHandler)clients.get(i);
			if(handler != this){ //postを行ったクライアント以外の時
				names.add(handler.getClientName()); //対象のユーザーを記憶
				//postを行なったクライアントの名前とメッセージを送る
				handler.send("["+this.getClientName()+"] "+message); 
			}
		}
		//postを行ったクライアントに、メッセージを受け取る相手を送る
		Collections.sort(names); //記憶した名前を並び替える
		String returnMessage = ""; 
		if(names.size() <=0) {
			returnMessage="no one receive message";
			send(returnMessage);
			System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
		} else{
			returnMessage="sent to ";
			for(int i=0; i<names.size(); i++){
				if(i>0) returnMessage=returnMessage+","; //最初のループ出ない時、","をメッセージに追加する
				returnMessage=returnMessage+names.get(i); //メッセージを受け取る相手の名前をメッセージに追加する
				send(returnMessage);
				System.out.println(": "+returnMessage); //サーバーに表示すべきものを表示する
			}
		}
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