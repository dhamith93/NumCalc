package dhamith.me.numcalc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private String memory;
    private String result;
    private int currentRadix, noOfProcs;

    private EditText input, tempResult;
    private TextView decResult, binResult, octResult, hexResult;

    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnA, btnB, btnC,
            btnD, btnE, btnF, btnAdd , btnSub, btnMult , btnDiv, btnEq, btnType, btnBack,
            btnMs, btnMc, btnMr, btnMAdd, btnMSub;

    private LinearLayout decimalView, binaryView, octalView, hexadecimalView, defaultView;

    private View dividerUnderDec, dividerUnderBin, dividerUnderOct, dividerUnderHex;

    private ScrollView scrollView;

    private final int RADIX_DEC = 10;
    private final int RADIX_BIN = 2;
    private final int RADIX_OCT = 8;
    private final int RADIX_HEX = 16;
    private final int SOLVE_ON_EQ = 0;
    private final int SOLVE_ON_FLY = 1;
    private boolean errorState;

    // (Only if noOfProcs <= 4): to make sure convert input only if input changed
    private boolean inputChanged;
    private boolean slideViewExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }

        setContentView(R.layout.activity_main);

        input = findViewById(R.id.mainInput);
        tempResult = findViewById(R.id.tempResult);
        scrollView = findViewById(R.id.scrollView);
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);

        input.setShowSoftInputOnFocus(false);
        tempResult.setShowSoftInputOnFocus(false);

        initButtons();
        initConversionViews();
        initResultLabels();
        setScrollViewHeight(false);

        currentRadix = RADIX_HEX;
        errorState = false;
        inputChanged = false;
        slideViewExpanded = false;

        noOfProcs = Runtime.getRuntime().availableProcessors();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                inputChanged = true;
                solveExpression(SOLVE_ON_FLY);
                if (noOfProcs > 4 || slideViewExpanded) {
                    convertInputOnFly();
                    inputChanged = false;
                }
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) { }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (noOfProcs <= 4 && inputChanged && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    convertInputOnFly();
                    inputChanged = false;
                }

                slideViewExpanded = (newState == SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        decimalView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                addToClipboard(ConversionDisplay.getBinResult());
                reveal(findViewById(R.id.inputLayout), v, R.color.copy, false,
                        new AnimatorListenerAdapter() {});
                return true;
            }
        });

        binaryView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                addToClipboard(ConversionDisplay.getBinResult());
                reveal(findViewById(R.id.inputLayout), v, R.color.copy, false,
                        new AnimatorListenerAdapter() {});
                return true;
            }
        });

        octalView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                addToClipboard(ConversionDisplay.getOctResult());
                reveal(findViewById(R.id.inputLayout), v, R.color.copy, false,
                        new AnimatorListenerAdapter() {});
                return true;
            }
        });

        hexadecimalView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                addToClipboard(ConversionDisplay.getHexResult());
                reveal(findViewById(R.id.inputLayout), v, R.color.copy, false,
                        new AnimatorListenerAdapter() {});
                return true;
            }
        });

        btnBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleClear();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        inputChanged = true;
        int start = input.getSelectionStart();
        if (errorState) {
            input.setText("");
            input.setTextColor(getResources().getColor(R.color.black));
            errorState = false;
        }

        switch (v.getId()) {
            // Numbers
            case R.id.btn_0:
                input.getText().insert(start,"0");
                break;
            case R.id.btn_1:
                input.getText().insert(start,"1");
                break;
            case R.id.btn_2:
                input.getText().insert(start,"2");
                break;
            case R.id.btn_3:
                input.getText().insert(start,"3");
                break;
            case R.id.btn_4:
                input.getText().insert(start,"4");
                break;
            case R.id.btn_5:
                input.getText().insert(start,"5");
                break;
            case R.id.btn_6:
                input.getText().insert(start,"6");
                break;
            case R.id.btn_7:
                input.getText().insert(start,"7");
                break;
            case R.id.btn_8:
                input.getText().insert(start,"8");
                break;
            case R.id.btn_9:
                input.getText().insert(start,"9");
                break;
            case R.id.btn_a:
                input.getText().insert(start,"A");
                break;
            case R.id.btn_b:
                input.getText().insert(start,"B");
                break;
            case R.id.btn_c:
                input.getText().insert(start,"C");
                break;
            case R.id.btn_d:
                input.getText().insert(start,"D");
                break;
            case R.id.btn_e:
                input.getText().insert(start,"E");
                break;
            case R.id.btn_f:
                input.getText().insert(start,"F");
                break;

            // Ops
            case R.id.btn_div:
                input.getText().insert(start,"÷");
                break;
            case R.id.btn_mult:
                input.getText().insert(start,"×");
                break;
            case R.id.btn_sub:
                input.getText().insert(start,"-");
                break;
            case R.id.btn_add:
                input.getText().insert(start,"+");
                break;
            case R.id.btn_eq:
                solveExpression(SOLVE_ON_EQ);
                break;

             // Calc Opts
            case R.id.btn_bks:
                handleDelete(start);
                break;

            case R.id.btn_ms:
                handleMemory("ms");
                break;
            case R.id.btn_mc:
                handleMemory("mc");
                break;
            case R.id.btn_mr:
                handleMemory("mr");
                break;
            case R.id.btn_m_add:
                handleMemory("m_add");
                break;
            case R.id.btn_m_sub:
                handleMemory("m_sub");
                break;

            case R.id.btn_type:
                handleConversion();
                break;
        }
    }

    private void handleDelete(int start) {
        int length = input.getText().length();
        if (length > 0 && (start - 1) > - 1) {
            input.getText().delete(start - 1, start);
        }
    }

    private void handleClear() {
        resetConversionViews();
        setScrollViewHeight(false);
        if (input.getText().length() > 0 || tempResult.getText().length() > 0) {
            reveal(findViewById(R.id.btn_bks), findViewById(R.id.inputLayout), R.color.colorAccent, true,
                    new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    input.setText("");
                    tempResult.setText("");
                }
            });
        }
    }

    private void handleConversion() {
        int oldRadix = currentRadix;
        changeType();
        setButtonsForType();
        convertInput(oldRadix);
        if (memory != null && !memory.isEmpty()) {
            Long n = Long.parseLong(memory, oldRadix);
            memory = Long.toString(n, currentRadix);
        }
    }

    private void solveExpression(int opt) {
        String in = StringUtils.convertOpsToNormal(input.getText().toString());

        if (in != null) {
            if (!StringUtils.isValidExpression(in)) {
                return;
            }

            Expression expression = new Expression(in, currentRadix);
            try {
                result = expression.result().toUpperCase();
                tempResult.setText("");

                if (opt == SOLVE_ON_EQ) {
                    input.setText("");
                    input.append(result);
                }

                if (opt == SOLVE_ON_FLY) {
                    tempResult.setText(result);
                }

            } catch (IllegalArgumentException ex) {
                if (opt == SOLVE_ON_EQ) {
                    if (ex.getMessage().equals("divide_by_0")) {
                        handleError("Can't divide by 0!");
                    } else {
                        handleError("Syntax error!");
                    }
                }
            } catch (Exception ignored) { }
        }
    }

    private void handleError(final String msg) {
        errorState = true;
        reveal(findViewById(R.id.btn_bks), findViewById(R.id.inputLayout), R.color.error, true,
                new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                input.setTextColor(getResources().getColor(R.color.error));
                input.setText(msg);
                tempResult.setText("");
            }
        });

    }

    private void convertInput(int oldRadix) {
        String in = input.getText().toString();
        if (!in.isEmpty()) {
            Expression expression = new Expression(StringUtils.convertOpsToNormal(in), oldRadix);
             try {
                 String converted = expression.convert(currentRadix).toUpperCase();
                 input.setText("");
                 input.append(StringUtils.convertOpsToUnicode(converted));
                 solveExpression(SOLVE_ON_FLY);
             } catch (RuntimeException ignored) { }
        }
    }

    private void convertInputOnFly() {
        resetConversionViews();
        setScrollViewHeight(false);

        String userIn = StringUtils.convertOpsToNormal(input.getText().toString());
        String tempIn = tempResult.getText().toString();

        if (userIn != null && ((!userIn.isEmpty() && StringUtils.isValidInput(userIn))
                || (!tempIn.isEmpty() && StringUtils.isValidExpression(userIn)))) {
            try {
                final String decValue;
                final String in;

                if (StringUtils.isValidExpression(userIn)) {
                    in = tempIn;
                } else {
                    in = userIn;
                }

                if (currentRadix != RADIX_DEC) {
                    Expression e = new Expression(in, currentRadix);
                    decValue = e.convert(RADIX_DEC);
                } else {
                    decValue = in;
                }

                defaultView.setVisibility(View.GONE);

                if (currentRadix != RADIX_DEC) {
                    Handler handler = new Handler(MainActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String decConverted = ConversionDisplay.buildForDecimal(in, currentRadix);
                            decimalView.setVisibility(View.VISIBLE);
                            dividerUnderDec.setVisibility(View.VISIBLE);
                            decResult.setText(Html.fromHtml(decConverted));
                        }
                    });
                }

                if (currentRadix != RADIX_BIN) {
                    Handler handler = new Handler(MainActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String binConverted = ConversionDisplay.buildForOther(decValue, RADIX_BIN);
                            binaryView.setVisibility(View.VISIBLE);
                            dividerUnderBin.setVisibility(View.VISIBLE);
                            binResult.setText(Html.fromHtml(binConverted));
                        }
                    });
                }

                if (currentRadix != RADIX_OCT) {
                    Handler handler = new Handler(MainActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String octConverted = ConversionDisplay.buildForOther(decValue, RADIX_OCT);
                            octalView.setVisibility(View.VISIBLE);
                            dividerUnderOct.setVisibility(View.VISIBLE);
                            octResult.setText(Html.fromHtml(octConverted));
                        }
                    });
                }

                if (currentRadix != RADIX_HEX) {
                    Handler handler = new Handler(MainActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String hexConverted = ConversionDisplay.buildForOther(decValue, RADIX_HEX);
                            hexadecimalView.setVisibility(View.VISIBLE);
                            dividerUnderHex.setVisibility(View.VISIBLE);
                            hexResult.setText(Html.fromHtml(hexConverted));
                        }
                    });
                }

                setScrollViewHeight(true);

            } catch (RuntimeException ignored) {
            }
        }
    }

    private void handleMemory(String opt) {
        switch (opt) {
            case "ms":
                memory = result;
                break;

            case "mc":
                memory = "";
                break;

            case "mr":
                if (memory != null && !memory.isEmpty()) {
                    input.append(memory.toUpperCase());
                }
                break;

            case "m_add":
                if (memory != null && !memory.isEmpty()
                        && result != null && !result.isEmpty()) {
                    Calculator calculator = new Calculator(currentRadix);
                    memory = calculator.solve("+", memory, result).toUpperCase();
                }
                break;

            case "m_sub":
                if (memory != null && !memory.isEmpty()
                        && result != null && !result.isEmpty()) {
                    Calculator calculator = new Calculator(currentRadix);
                    memory = calculator.solve("-", memory, result).toUpperCase();
                }
                break;
        }
    }

    private void addToClipboard(String result) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("result", result);
        clipboard.setPrimaryClip(clip);

        showToast("Copied to clipboard.");
    }

    private void changeType() {
        switch (currentRadix) {
            case RADIX_DEC:
                currentRadix = RADIX_BIN;
                break;

            case RADIX_BIN:
                currentRadix = RADIX_OCT;
                break;

            case RADIX_OCT:
                currentRadix = RADIX_HEX;
                break;

            case RADIX_HEX:
                currentRadix = RADIX_DEC;
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setButtonsForType() {
        enableAllButtons();

        if (currentRadix == RADIX_DEC) {
            disableNonDecButtons();
            btnType.setText("DEC");
        }

        if (currentRadix == RADIX_BIN) {
            disableNonBinButtons();
            btnType.setText("BIN");
        }

        if (currentRadix == RADIX_OCT) {
            disableNonOctButtons();
            btnType.setText("OCT");
        }

        if (currentRadix == RADIX_HEX) btnType.setText("HEX");
    }

    private void disableNonDecButtons() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);
        btnF.setEnabled(false);
    }

    private void disableNonBinButtons() {
        disableNonDecButtons();
        disableNonOctButtons();

        btn7.setEnabled(false);
        btn6.setEnabled(false);
        btn5.setEnabled(false);
        btn4.setEnabled(false);
        btn3.setEnabled(false);
        btn2.setEnabled(false);
    }

    private void disableNonOctButtons() {
        disableNonDecButtons();

        btn9.setEnabled(false);
        btn8.setEnabled(false);
    }

    private void enableAllButtons() {
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
        btnE.setEnabled(true);
        btnF.setEnabled(true);

        btn9.setEnabled(true);
        btn8.setEnabled(true);
        btn7.setEnabled(true);
        btn6.setEnabled(true);
        btn5.setEnabled(true);
        btn4.setEnabled(true);
        btn3.setEnabled(true);
        btn2.setEnabled(true);
        btn1.setEnabled(true);
        btn0.setEnabled(true);
    }

    private void setScrollViewHeight(boolean afterResult) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;

        int height = (afterResult) ? (int) dpHeight : ViewGroup.LayoutParams.WRAP_CONTENT;

        scrollView.measure(0, 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height);
        scrollView.setLayoutParams(lp);
    }

    private void reveal(View sourceView, View v, int colorRes, boolean fillStatusBar, Animator.AnimatorListener listener) {
        final ViewGroupOverlay groupOverlay =
                (ViewGroupOverlay) getWindow().getDecorView().getOverlay();

        final Rect displayRect = new Rect();

        if (v == null){
            return;
        }
        v.getGlobalVisibleRect(displayRect);

        // Make reveal cover the display and status bar.
        final View revealView = new View(this);
        revealView.setBottom(displayRect.bottom);
        revealView.setLeft(displayRect.left);
        revealView.setRight(displayRect.right);
        if (!fillStatusBar) {
            revealView.setTop(displayRect.top);
        }
        revealView.setBackgroundColor(getResources().getColor(colorRes));
        groupOverlay.add(revealView);

        final int[] clearLocation = new int[2];
        sourceView.getLocationInWindow(clearLocation);
        clearLocation[0] += sourceView.getWidth();
        clearLocation[1] += sourceView.getHeight();

        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop() * 2;

        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2) * 2;
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        final Animator revealAnimator =
                ViewAnimationUtils.createCircularReveal(revealView,
                        revealCenterX, revealCenterY, 0.0f, revealRadius);
        revealAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        revealAnimator.addListener(listener);

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(revealAnimator).before(alphaAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private void initConversionViews() {
        decimalView = findViewById(R.id.decimal_conversion_view);
        binaryView = findViewById(R.id.binary_conversion_view);
        octalView = findViewById(R.id.octal_conversion_view);
        hexadecimalView = findViewById(R.id.hexadecimal_conversion_view);
        defaultView = findViewById(R.id.default_conversion_view);

        dividerUnderDec = findViewById(R.id.dividerUnderDec);
        dividerUnderBin = findViewById(R.id.dividerUnderBin);
        dividerUnderOct = findViewById(R.id.dividerUnderOct);
        dividerUnderHex = findViewById(R.id.dividerUnderHex);

        resetConversionViews();
    }

    private void resetConversionViews() {
        decimalView.setVisibility(View.GONE);
        binaryView.setVisibility(View.GONE);
        octalView.setVisibility(View.GONE);
        hexadecimalView.setVisibility(View.GONE);
        defaultView.setVisibility(View.VISIBLE);

        dividerUnderDec.setVisibility(View.GONE);
        dividerUnderBin.setVisibility(View.GONE);
        dividerUnderOct.setVisibility(View.GONE);
        dividerUnderHex.setVisibility(View.GONE);
    }

    private void initButtons() {
        btn0 = findViewById(R.id.btn_0);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
        btnA = findViewById(R.id.btn_a);
        btnB = findViewById(R.id.btn_b);
        btnC = findViewById(R.id.btn_c);
        btnD = findViewById(R.id.btn_d);
        btnE = findViewById(R.id.btn_e);
        btnF = findViewById(R.id.btn_f);

        btnAdd = findViewById(R.id.btn_add);
        btnSub = findViewById(R.id.btn_sub);
        btnMult = findViewById(R.id.btn_mult);
        btnDiv = findViewById(R.id.btn_div);
        btnEq = findViewById(R.id.btn_eq);
        btnType = findViewById(R.id.btn_type);
        btnBack = findViewById(R.id.btn_bks);

        btnMs = findViewById(R.id.btn_ms);
        btnMr = findViewById(R.id.btn_mr);
        btnMc = findViewById(R.id.btn_mc);
        btnMAdd = findViewById(R.id.btn_m_add);
        btnMSub = findViewById(R.id.btn_m_sub);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
        btnE.setOnClickListener(this);
        btnF.setOnClickListener(this);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnEq.setOnClickListener(this);
        btnType.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnMs.setOnClickListener(this);
        btnMr.setOnClickListener(this);
        btnMc.setOnClickListener(this);
        btnMAdd.setOnClickListener(this);
        btnMSub.setOnClickListener(this);
    }

    private void initResultLabels() {
        decResult = findViewById(R.id.decimal_result);
        binResult = findViewById(R.id.binary_result);
        octResult = findViewById(R.id.octal_result);
        hexResult = findViewById(R.id.hexadecimal_result);
    }
}
