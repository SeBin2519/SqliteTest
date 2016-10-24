package kr.hs.emirim.sebin2519.sqlitetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editName, editCnt, editNameResult, editCntResult;
    Button butInit, butInput,butSel;
    MyDBHelper dbHelper;
    SQLiteDatabase db; //저장할 거

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper=new MyDBHelper(getApplicationContext()); //객체 생성

        setContentView(R.layout.activity_main);

        editName=(EditText)findViewById(R.id.edit_groupname);
        editCnt=(EditText)findViewById(R.id.edit_groupcnt);
        editNameResult=(EditText)findViewById(R.id.edit_name_result);
        editCntResult=(EditText)findViewById(R.id.edit_cnt_result);
        butInit=(Button)findViewById(R.id.but_init);
        butInput=(Button)findViewById(R.id.but_input);
        butSel=(Button)findViewById(R.id.but_select);

        //초기화 버튼 클릭할 떄
        butInit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase(); //데이터가 변경
                dbHelper.onUpgrade(db, 1, 2);//초기화하면 버전 바뀜
                db.close();
                Toast.makeText(getApplicationContext(),"초기화 되었습니다!",Toast.LENGTH_SHORT).show();
            }
        });

        //입력 버튼 클릭할 떄
        butInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db=dbHelper.getWritableDatabase();
                db.execSQL("insert into idoltable values('"
                        +editName.getText().toString()+"', "
                        +editCnt.getText().toString()+
                        ");");//변경되는것
                db.close();
                Toast.makeText(getApplicationContext(),"정상적으로 입력이 되었습니다!",Toast.LENGTH_SHORT).show();
            }
        });

        //조회 버튼 클릭할 떄
        butSel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               db=dbHelper.getWritableDatabase();//반환받음
                Cursor rs=db.rawQuery("select * from idoltable",null);//커서 반환 == result set;과 같음
                String gname="그룹이름"+"\n"+"-------------------"+"\n";//머리글 행
                String gCnt="인원 수"+"\n"+"-------------------"+"\n";//머리글 행
                //출력해서 보여주기
                while(rs.moveToNext()) { //커서를 이동하면서 있으면 true,  없으면 false
                   //누적시키기
                    gname+=rs.getString(0)+"\n";//첫번째 컬럼 0
                    gCnt+=rs.getInt(1)+"\n";
                }
                //editText에 추가
                editNameResult.setText(gname);
                editCntResult.setText(gCnt);

                rs.close();
                db.close();
            }
        });

    }

    //내부클래스
    public class MyDBHelper extends SQLiteOpenHelper{

        //생성자
        public MyDBHelper(Context context){
            super(context,"idoldb",null,1 );//idoldb: db이름, 1: 버전
        }
        @Override
        public void onCreate(SQLiteDatabase db) { //db가 생성되면 호출
            //테이블 생성
            db.execSQL("create table idoltable(gname char(40) primary key, gcnt integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //기존 테이블이 존재할시 새로운 db를 만들고 기존 테이블 삭제
            db.execSQL("drop table if exists idoltable"); //테이블이 존재하면 테이블을 삭제할게요!
            onCreate(db);
        }
    }

}
