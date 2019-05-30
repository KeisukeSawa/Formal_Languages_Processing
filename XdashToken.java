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

        // id
        int id;

        Path path = Paths.get("Sample.txt");
        
        List<String> code = Files.readAllLines(path);

        // 入力されたプログラムの行数だけ取り出す。
        for(int i=0; i<code.size(); i++){

            // 1行だけ取り出す。
            String row = code.get(i);
            
            // 各要素に分解する
            List<String> element = Row_split(row);

            // 要素の個数だけ繰り返す
            for(int j = 0; j < element.size(); j++){

                pre_type_number = type_number;

                // 種類の判定をする
                while(true){

                    // 種類１
                    id = Judgment(element.get(j), reserved_word, type1);
                    if(id != -1){
                        type_number = 1;
                        Output(type_number, id, j, 1, 0, element, type1);
                        break;

                    }

                    // 種類４
                    id = Judgment(element.get(j), type, type4);
                    if(id != -1){
                        type_number = 4;
                        Output(type_number, id, j, 1, 0, element, type4);
                        break;
                    }

                    // 種類６
                    id = Judgment(element.get(j), function, type6);
                    if(id != -1){
                        type_number = 6;
                        Output(type_number, id, j, 1, 0, element, type6);
                        break;
                    }

                    // 種類８（普通の演算子）
                    id = Judgment(element.get(j), operator, type8_nomal);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, id, j, 1, 0, element, type8_nomal);
                        break;
                    }

                    // 種類８（比較演算子）
                    id = Judgment(element.get(j), operater_comp, type8_comp);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, id, j, 11, 10, element, type8_comp);
                        break;
                    }
                    
                    // 種類２
                    if(pre_type_number == 1){
                        type_number = 2;
                        id = Search(element.get(j), type2);
                        Output(type_number, id, j, 1, 0, element, type4);
                        break;
                    }

                    // 種類７
                    if(pre_type_number == 8){
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

                    // 種類９

                    break;


                }

                
            }
            
            

        }   
        

    }

    /*
    関数名：Output
    引数：int type_number, int id, int j, int pad, List<String> element, List<String> type
    戻り値：なし
    役割：コンソールに決まった形式に従って、出力させる。
    　　　typeに語句が追加されていなければ、登録する。
    */

    static void Output(int type_number, int id, int suffix, int pading, int pading2, List<String> element, List<String> type){

        if(id != -2){
            System.out.println("(" + type_number + "," + (id+pading) + ")");
        }
        else{
            type.add(element.get(suffix));
            System.out.println("(" + type_number + "," + (type.size()+pading2) + ")");
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

        int judgement = -2;

        for(int i=0; i < type.size(); i++){
            if(target.equals(type.get(i))){
                judgement = i;
                return judgement;
            }
        }

        return judgement;

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

        int judgement = -1;

        for(int i=0; i<judge.length; i++){

            if(target.equals(judge[i])){

                for(int k=0; k < type.size(); k++){
                    if(target.equals(type.get(k))){
                        judgement = k;
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

        List<String> element = new ArrayList<String>();

        // rowの空白、制御文字の削除
        row = row.trim();

        int left = 0, right = 0;

        for(int i=0; i<=row.length(); i++){

            if(right == row.length() ){

                String cut = row.substring(left, right);
                
                if(cut.length() != 0){
                    element.add(cut);
                    System.out.println(cut);
                }
                
            }
            else if( (row.charAt(i) == ' ') || (row.charAt(i) == ',')  || (row.charAt(i) == ';') || (row.charAt(i) == ')')  || (row.charAt(i) == '}') ){

                String cut = row.substring(left, right);

                if(cut.length() != 0){
                    element.add(cut);
                    System.out.println(cut);
                }
                left = i+1;
                right = i+1;
                
            }
            else if ( (row.charAt(i) == '(') ||  (row.charAt(i) == '{') ){

                left = i+1;
                right = i+1;

            }
            else {
                
                right++;

            }

        }
        
        return element;

    }

}
