// 演習１プログラム
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class XdashToken{
    public static void main(String[] args) throws IOException {

        // 事前に決められている演算子などの語を配列にまとめる
        // 予約語配列
        String[] reserved_word = {"program","while","if"};
        // 型名配列
        String[] type = {"int","String","char"};
        // 関数配列
        String[] function = {"print"};
        // 演算子配列
        String[] operator = {"+","-",":="};
        String[] operater_comp = {"<","<=",">=",">"};
        // コメント配列
        String[] comment = {"//","/*","*/"};

        // 各種類に分類された要素を格納するリスト
        // 種類１（予約語）
        List<String> type1 = new ArrayList<String>();
        // 種類２（識別子：プログラム名）
        List<String> type2 = new ArrayList<String>();
        // 種類４（型名）
        List<String> type4 = new ArrayList<String>();
        // 種類６（関数名）
        List<String> type6 = new ArrayList<String>();
        // 種類７（定数）
        List<String> type7 = new ArrayList<String>();
        // 種類８（普通の演算子）
        List<String> type8_nomal = new ArrayList<String>();
        // 種類８（比較演算子）
        List<String> type8_comp = new ArrayList<String>();
        // 種類９（識別子：変数名）
        List<String> type9 = new ArrayList<String>();

        // トークンの種類番号
        int type_number = 0;
        // 前の要素の種類番号
        int pre_type_number = 0;
        // commentが次の行に続いているかを判定
        int comment_flag = 0;
        // その行に種類4があるかどうかを判定
        int type4_flag = 0;
        // id
        int id;

        // ファイルの内容をgetし、1行ずつString型のリストcodeに代入
        Path path = Paths.get("Sample.txt");
        List<String> code = Files.readAllLines(path);

        // 入力されたプログラムの行数だけ繰り返す
        for(int i=0; i<code.size(); i++){
            // 1行だけ取り出す。
            String row = code.get(i);
            // 各要素に分解する
            List<String> element = Row_split(row);

            // 要素の個数だけ繰り返す
            for(int j = 0; j < element.size(); j++){
                // 1つ前の要素の種類番号を更新 
                pre_type_number = type_number;

                // 種類の判定
                while(true){
                    // コメントの判定(*/)
                    if(element.get(j).equals("*/")){
                        // コメントの終了
                        comment_flag = 0;
                        break;
                    }

                    // comment_flagが1である間は判定を行わない
                    if(comment_flag == 1){
                        break;
                    }

                    // コメントの判定(/*)
                    if(element.get(j).equals("/*")){
                        // コメントの開始
                        comment_flag = 1;
                        break;
                    }

                    // コメントの判定をする(//)
                    if(element.get(j).equals("//")){
                        // for文の終了条件を指定し、この行の以降の判定を行わないようにする
                        j = element.size();
                        break;
                    }

                    // 種類１(予約語)
                    // 現在の要素がreserved_wordに格納されているか調べる
                    id = Judgment(element.get(j), reserved_word, type1);
                    //　格納されているとき実行
                    if(id != -1){
                        // 種類番号の更新
                        type_number = 1;
                        // 結果の出力
                        Output(type_number, element.get(j), id, 1, type1);
                        // while文から抜ける
                        break;
                    }

                    // 種類４(型名)
                    id = Judgment(element.get(j), type, type4);
                    if(id != -1){
                        type_number = 4;
                        Output(type_number, element.get(j), id, 1, type4);
                        // type4_flagに1を代入（型の宣言をするため）
                        type4_flag = 1;
                        break;
                    }

                    // 種類６（関数名）
                    id = Judgment(element.get(j), function, type6);
                    if(id != -1){
                        type_number = 6;
                        Output(type_number, element.get(j), id, 1, type6);
                        break;
                    }

                    // 種類８（普通の演算子）
                    id = Judgment(element.get(j), operator, type8_nomal);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, element.get(j), id, 1, type8_nomal);
                        break;
                    }

                    // 種類８（比較演算子）
                    id = Judgment(element.get(j), operater_comp, type8_comp);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, element.get(j), id, 11, type8_comp);
                        break;
                    }

                    // 種類９
                    type_number = 9;
                    // type9に現在の要素が格納されているか調べる
                    id = Search(element.get(j), type9);
                    // 格納されていたときに実行
                    if(id != -2){
                        // 結果を出力
                        System.out.println("(" + type_number + "," + (id+1) + ")");
                        break;
                    }
                    // type9に現在の要素が格納されていないとき
                    else{
                        // この行でtype4の要素があった場合（型名が宣言されていた場合）
                        if(type4_flag == 1){
                            // type9に現在の要素を追加する
                            type9.add(element.get(j));
                            // 結果を出力
                            System.out.println("(" + type_number + "," + type9.size() + ")");
                            break;
                        }
                        
                    }

                    // 種類２
                    if(pre_type_number == 1){
                        // 現在の要素がtype9で宣言されている変数名ではなかったらtype2と判断する
                        id = Search(element.get(j), type9);
                        if(id == -2){
                            type_number = 2;
                            id = Search(element.get(j), type2);
                            Output(type_number, element.get(j), id, 1, type2);
                            break;
                        }
                    }
                    
                    // 種類７
                    if(pre_type_number == 8 || pre_type_number == 6 ){
                        type_number = 7;
                        id = Search(element.get(j), type7);
                        if(id != -2){
                            System.out.println("(" + type_number + "," + element.get(j) + ")");
                            break;
                        }
                        else{
                            type7.add(element.get(j));
                            System.out.println("(" + type_number + "," + element.get(j) + ")");
                            break;
                        }
                    }
                }    
            }
            // 行が変わるため、type4_flagを0に戻す
            type4_flag = 0;
        }   
    }

    /*
    関数名：Output
    引数：int type_number, String element, int id, int pading, List<String> type
    戻り値：なし
    役割：コンソールに形式に従って、結果を出力させる。
          種類8では、種類ごとのid番号に特徴があるので、padingでそれを設定する
    　　　 elememtがtypeに追加されていなければ、登録する。
    */
    static void Output(int type_number, String element, int id, int pading, List<String> type){

        if(id != -2){
            // 結果を出力
            System.out.println("(" + type_number + "," + (id+pading) + ")");
        }
        else{
            // typeにelementを追加
            type.add(element);
            // 結果を出力
            System.out.println("(" + type_number + "," + (type.size()+pading-1) + ")");
        }
    }

    /*
    関数名：Serch
    引数：String target List<String> type
    戻り値：int
    役割：targetがtypeに格納されていないかどうかを調べる。
         格納されていれば、その添字を返す。そうでなければ-2を返す。
    */
    static int Search(String target, List<String> type){

        // searchに―2を代入（格納されていないと仮定）
        int search = -2;

        // typeのサイズだけ繰り返す
        for(int i=0; i < type.size(); i++){
            // targetがtypeのi番目の要素と等しいならば実行
            if(target.equals(type.get(i))){
                // searchをiにする
                search = i;
                // judgementを返す
                return search;
            }
        }
        return search;
    }
    
    /*
    関数名：Judgment
    引数：String target, String[] judge,List<String> type
    戻り値：int
    役割：targetにjudge配列のデータが格納されていないかどうか調べる。
    　　　もし、格納されていなければ、-1を返り値として返す。
         格納されていて、それがすでにtypeに登録されていれば、その添字を返す。
         そうでなければ、-2を返り値として返す。
    */
    static int Judgment(String target, String[] judge, List<String> type){

        // judmentに―1を代入する（格納されていないと仮定する）
        int judgement = -1;

        // judge配列の長さだけ繰り返す
        for(int i=0; i<judge.length; i++){

            // judge[i]とtargetが等しければ実行
            if(target.equals(judge[i])){
                // typeのサイズだけ繰り返す
                for(int k=0; k < type.size(); k++){
                    // もし、typeにtargetが格納されていれば実行
                    if(target.equals(type.get(k))){
                        // judgementを添字にする
                        judgement = k;
                        // judgementを返す
                        return judgement;
                    }
                }
                judgement = -2;
                return judgement;
            }
        }
        return judgement;
    }

    /* 
    関数名：Row_split
    引数：String row
    戻り値：String[]
    役割：rowを要素ごとに分割し、要素ごとにString型の配列に格納し、返り値として返す。
         その際、区切り文字のコンマ, セミコロン; ブロックを表す curly blacket　( {　と } )および 括弧 ( ) は読み飛ばす。　
    */
    static List<String> Row_split(String row){

        // rowの中身を要素ごとに格納する配列
        List<String> element = new ArrayList<String>();

        // rowの空白、制御文字の削除
        row = row.trim();

        // rowから要素を取り出す際のrowの左側、右側の添字
        int left = 0, right = 0;

        // rowの長さだけ繰り返す
        for(int i=0; i<=row.length(); i++){
            // rowの最後の文字に来たら実行
            if(right == row.length() ){
                // cutにrowのleft番目の文字からright番目の文字を切り出して代入
                String cut = row.substring(left, right);
                // cutの長さが0でない（cutが空文字でなければ）実行
                if(cut.length() != 0){
                    // elementにcutを代入
                    element.add(cut);
                }
            }
            // rowのi番目の文字が ' '　','　';' ')'　'}'　であれば実行する（これらの文字は無視）
            else if( (row.charAt(i) == ' ') || (row.charAt(i) == ',')  || (row.charAt(i) == ';') || (row.charAt(i) == ')')  || (row.charAt(i) == '}') ){
                String cut = row.substring(left, right);
                if(cut.length() != 0){
                    element.add(cut);
                }
                // left,rightをi+1に設定
                left = i+1;
                right = i+1;
            }
            // rowのi番目の文字が '(' '{'　であれば実行
            else if ( (row.charAt(i) == '(') ||  (row.charAt(i) == '{') ){
                left = i+1;
                right = i+1;
            }
            else {
                // rightを1増やす
                right++;
            }
        }
        // elementを返す
        return element;
    }
}
