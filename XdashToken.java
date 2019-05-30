import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class XdashToken{

    public static void main(String[] args) throws IOException {

        // ���O�Ɍ��߂��Ă��鉉�Z�q�Ȃǂ̌��z��ɂ܂Ƃ߂�

        // �\���z��
        String[] reserved_word = {"program","while","if"};

        // �^���z��
        String[] type = {"int","String","char"};

        // �֐��z��
        String[] function = {"print"};

        // ���Z�q�z��
        String[] operator = {"+","-",":="};
        String[] operater_comp = {"<","<=",">=",">"};

        // �R�����g�z��
        String[] comment = {"//","/*","*/"};

        // �e��ނɕ��ނ��ꂽ�v�f���i�[���郊�X�g

        // ��ނP�i�\���j
        List<String> type1 = new ArrayList<String>();

        // ��ނQ�i���ʎq�F�v���O�������j
        List<String> type2 = new ArrayList<String>();

        // ��ނS�i�^���j
        List<String> type4 = new ArrayList<String>();

        // ��ނU�i�֐����j
        List<String> type6 = new ArrayList<String>();

        // ��ނV�i�萔�j
        List<String> type7 = new ArrayList<String>();

        // ��ނW�i���ʂ̉��Z�q�j
        List<String> type8_nomal = new ArrayList<String>();

        // ��ނW�i��r���Z�q�j
        List<String> type8_comp = new ArrayList<String>();

        // ��ނX�i���ʎq�F�ϐ����j
        List<String> type9 = new ArrayList<String>();

        // �g�[�N���̎�ޔԍ�
        int type_number = 0;

        // �O�̗v�f�̎�ޔԍ�
        int pre_type_number = 0;

        // id
        int id;

        Path path = Paths.get("Sample.txt");
        
        List<String> code = Files.readAllLines(path);

        // ���͂��ꂽ�v���O�����̍s���������o���B
        for(int i=0; i<code.size(); i++){

            // 1�s�������o���B
            String row = code.get(i);
            
            // �e�v�f�ɕ�������
            List<String> element = Row_split(row);

            // �v�f�̌������J��Ԃ�
            for(int j = 0; j < element.size(); j++){

                pre_type_number = type_number;

                // ��ނ̔��������
                while(true){

                    // ��ނP
                    id = Judgment(element.get(j), reserved_word, type1);
                    if(id != -1){
                        type_number = 1;
                        Output(type_number, id, j, 1, 0, element, type1);
                        break;

                    }

                    // ��ނS
                    id = Judgment(element.get(j), type, type4);
                    if(id != -1){
                        type_number = 4;
                        Output(type_number, id, j, 1, 0, element, type4);
                        break;
                    }

                    // ��ނU
                    id = Judgment(element.get(j), function, type6);
                    if(id != -1){
                        type_number = 6;
                        Output(type_number, id, j, 1, 0, element, type6);
                        break;
                    }

                    // ��ނW�i���ʂ̉��Z�q�j
                    id = Judgment(element.get(j), operator, type8_nomal);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, id, j, 1, 0, element, type8_nomal);
                        break;
                    }

                    // ��ނW�i��r���Z�q�j
                    id = Judgment(element.get(j), operater_comp, type8_comp);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, id, j, 11, 10, element, type8_comp);
                        break;
                    }
                    
                    // ��ނQ
                    if(pre_type_number == 1){
                        type_number = 2;
                        id = Search(element.get(j), type2);
                        Output(type_number, id, j, 1, 0, element, type4);
                        break;
                    }

                    // ��ނV
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

                    // ��ނX

                    break;


                }

                
            }
            
            

        }   
        

    }

    /*
    �֐����FOutput
    �����Fint type_number, int id, int j, int pad, List<String> element, List<String> type
    �߂�l�F�Ȃ�
    �����F�R���\�[���Ɍ��܂����`���ɏ]���āA�o�͂�����B
    �@�@�@type�Ɍ�傪�ǉ�����Ă��Ȃ���΁A�o�^����B
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
    �֐����FSerch
    �����FString target List<String> type
    �߂�l�Fint
    �����Ftarget��type�Ɋi�[����Ă��Ȃ����ǂ����𒲂ׂ�B
          �i�[����Ă���΁A���̓Y����Ԃ��B�����łȂ����-2��Ԃ��B
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
    �֐����FJudgment
    �����FString target, String[] judge,List<String> type
    �߂�l�Fint
    �����Ftarget��judge�z��̃f�[�^���i�[����Ă��Ȃ����ǂ������ׂ�B
    �@�@�@�����A�i�[����Ă��Ȃ���΁A-1��Ԃ�l�Ƃ��ĕԂ��B
         �i�[����Ă��āA���ꂪ���ł�type�ɓo�^����Ă���΁A���̓Y����Ԃ��B
         �����łȂ���΁A-2��Ԃ�l�Ƃ��ĕԂ��B
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
    �֐����FRow_split
    �����FString row
    �߂�l�FString[]
    �����Frow��v�f���Ƃɕ������A�v�f���Ƃ�String�^�̔z��Ɋi�[���A�Ԃ�l�Ƃ��ĕԂ��B
         ���̍ہA��؂蕶���̃R���}, �Z�~�R����; �u���b�N��\�� curly blacket�@( {�@�� } )����� ���� ( ) �͓ǂݔ�΂��B�@
    */
    static List<String> Row_split(String row){

        List<String> element = new ArrayList<String>();

        // row�̋󔒁A���䕶���̍폜
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
