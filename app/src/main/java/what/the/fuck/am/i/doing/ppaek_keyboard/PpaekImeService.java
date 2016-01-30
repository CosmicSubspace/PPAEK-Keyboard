package what.the.fuck.am.i.doing.ppaek_keyboard;

import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class PpaekImeService extends InputMethodService
        implements View.OnClickListener, View.OnTouchListener {

    private static final String LOG_TAG = "PPAEK";

    String start="빼", middle="애", end="액";

    String[][] strs={{"빼","애","액"},{"히","이","익"},{"으","아","앍"},{"으","아","앙"},{"으","아","아"},{"하","아","앍"},{"흐","이","잇"},{"후","에","엥"},{"헤","에","?"},{"호","오","?"}};
    int idx=0;

    private View v;

    Button mainBtn, qBtn, cBtn, eBtn, dBtn, delBtn, spaceBtn, enterBtn;
    Button prevBtn,nextBtn;

    private int repeats = 0;


    @Override
    public View onCreateInputView() {
        v = getLayoutInflater().inflate(R.layout.layout, null);
        mainBtn = (Button) v.findViewById(R.id.key_ppaek);
        mainBtn.setOnTouchListener(this);
        qBtn = (Button) v.findViewById(R.id.key_q);
        qBtn.setOnClickListener(this);
        cBtn = (Button) v.findViewById(R.id.key_c);
        cBtn.setOnClickListener(this);
        eBtn = (Button) v.findViewById(R.id.key_e);
        eBtn.setOnClickListener(this);
        dBtn = (Button) v.findViewById(R.id.key_d);
        dBtn.setOnClickListener(this);
        delBtn = (Button) v.findViewById(R.id.key_del);
        delBtn.setOnTouchListener(this);

        nextBtn = (Button) v.findViewById(R.id.next);
        nextBtn.setOnClickListener(this);
        prevBtn = (Button) v.findViewById(R.id.prev);
        prevBtn.setOnClickListener(this);

        spaceBtn = (Button) v.findViewById(R.id.key_space);
        spaceBtn.setOnClickListener(this);
        enterBtn = (Button) v.findViewById(R.id.key_end);
        enterBtn.setOnClickListener(this);


        return v;
    }

    public void ppaek() {
        InputConnection ic = getCurrentInputConnection();


        if (repeats == 0) {
            ic.setComposingText(start, 1);
        } else {
            StringBuilder sb = new StringBuilder(start);
            for (int i = 0; i < repeats; i++) {
                sb.append(middle);
            }
            sb.append(end);
            ic.setComposingText(sb.toString(), 1);

        }
        repeats++;
    }

    public void endPpaek() {
        InputConnection ic = getCurrentInputConnection();

        if (repeats == 0) {
            ic.commitText(start, 1);
        } else {
            StringBuilder sb = new StringBuilder(start);
            for (int i = 0; i < repeats-1; i++) {
                sb.append(middle);
            }
            sb.append(end);
            ic.commitText(sb.toString(), 1);

        }
        repeats = 0;
    }

    public void setText(String start, String middle, String end){
        this.start=start;
        this.middle=middle;
        this.end=end;
        mainBtn.setText(start+middle+end);
    }

    private void setTextFromList(){
        while (idx<0) idx+=strs.length;

        setText(strs[idx%strs.length][0],strs[idx%strs.length][1],strs[idx%strs.length][2]);
    }


    public void delete(){
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }



    @Override
    public void onClick(View v) {
        InputConnection ic = getCurrentInputConnection();
        int id = v.getId();
        if (id == R.id.key_e) { //!
            ic.commitText("!", 1);
        } else if (id == R.id.key_q) { //?
            ic.commitText("?", 1);
        } else if (id == R.id.key_d) { //.
            ic.commitText(".", 1);
        } else if (id == R.id.key_c) { //,
            ic.commitText(",", 1);
        } else if (id == R.id.key_del) { //!

        } else if (id == R.id.key_space) { //!
            ic.commitText(" ", 1);
        } else if (id == R.id.key_end) { //!
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        }else if (id == R.id.next) { //!
            idx++;
            setTextFromList();
        }else if (id == R.id.prev) { //!
            idx--;
            setTextFromList();
        }

    }

    Handler mHandler = null;

    Runnable ppaek_action = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 50);
            ppaek();
        }
    };

    Runnable del_action = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 50);
            delete();
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId()==R.id.key_ppaek) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mHandler != null) return true;
                    mHandler = new Handler();
                    mHandler.postDelayed(ppaek_action, 50);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandler == null) return true;
                    mHandler.removeCallbacks(ppaek_action);
                    mHandler = null;
                    endPpaek();
                    break;
            }
        }else if (v.getId()==R.id.key_del) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mHandler != null) return true;
                    mHandler = new Handler();
                    mHandler.postDelayed(del_action, 50);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandler == null) return true;
                    mHandler.removeCallbacks(del_action);
                    mHandler = null;
                    break;
            }
        }
        return false;


    }

}