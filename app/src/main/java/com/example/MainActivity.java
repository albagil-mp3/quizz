package com.example;

import com.example.quizz.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private TextView answeredCounterTextView;
    private TextView incorrectCounterTextView;
    private Button btnTrue;
    private Button btnFalse;
    private Button btnNext;
    private Button btnPrevious;

    private ArrayList<Question> questionList;
    private int currentQuestionIndex = 0;
    private boolean[] answeredQuestions;
    private Boolean[] userAnswers; // null = no respondida, true/false = respuesta del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        imageView = findViewById(R.id.imageView);
        questionTextView = findViewById(R.id.questionTextView);
        questionCounterTextView = findViewById(R.id.questionCounterTextView);
        answeredCounterTextView = findViewById(R.id.answeredCounterTextView);
        incorrectCounterTextView = findViewById(R.id.incorrectCounterTextView);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        // Inicializar preguntas
        initializeQuestions();

        // Restaurar estado si existe (por ejemplo, después de rotar la pantalla)
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0);
            answeredQuestions = savedInstanceState.getBooleanArray("answeredQuestions");

            // Restaurar las respuestas del usuario
            boolean[] tempAnswers = savedInstanceState.getBooleanArray("userAnswersValues");
            boolean[] tempAnswersSet = savedInstanceState.getBooleanArray("userAnswersSet");

            userAnswers = new Boolean[questionList.size()];
            if (tempAnswers != null && tempAnswersSet != null) {
                for (int i = 0; i < questionList.size(); i++) {
                    if (tempAnswersSet[i]) {
                        userAnswers[i] = tempAnswers[i];
                    }
                }
            }
        } else {
            userAnswers = new Boolean[questionList.size()];
            answeredQuestions = new boolean[questionList.size()];
        }

        // Configurar listeners
        setupListeners();

        // Mostrar pregunta actual
        displayQuestion();
    }

    private void initializeQuestions() {
        questionList = new ArrayList<>();

        // Agregar preguntas de ejemplo
        questionList.add(new Question(
            R.drawable.img_sun,
            "¿La imagen muestra el sol?",
            true
        ));

        questionList.add(new Question(
            R.drawable.img_moon,
            "¿La imagen muestra el sol?",
            false
        ));

        questionList.add(new Question(
            R.drawable.img_tree,
            "¿La imagen muestra un árbol?",
            true
        ));

        questionList.add(new Question(
            R.drawable.img_ocean,
            "¿La imagen muestra un desierto?",
            false
        ));

        questionList.add(new Question(
            R.drawable.img_rain,
            "¿La imagen muestra nubes con lluvia?",
            true
        ));
    }

    private void setupListeners() {
        btnTrue.setOnClickListener(v -> checkAnswer(true));
        btnFalse.setOnClickListener(v -> checkAnswer(false));

        btnNext.setOnClickListener(v -> {
            currentQuestionIndex++;
            if (currentQuestionIndex >= questionList.size()) {
                currentQuestionIndex = 0; // Volver a la primera pregunta
            }
            displayQuestion();
        });

        btnPrevious.setOnClickListener(v -> {
            currentQuestionIndex--;
            if (currentQuestionIndex < 0) {
                currentQuestionIndex = questionList.size() - 1; // Ir a la última pregunta
            }
            displayQuestion();
        });
    }

    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);


        // Mostrar imagen y pregunta
        imageView.setImageResource(currentQuestion.getImageResId());
        questionTextView.setText(currentQuestion.getQuestionText());

        // Actualizar contador de pregunta actual
        questionCounterTextView.setText(getString(R.string.question_counter,
                                                  currentQuestionIndex + 1,
                                                  questionList.size()));

        // Actualizar contadores
        updateAnsweredCounter();
        updateIncorrectCounter();

        // Restaurar el estado visual de la respuesta dada
            Boolean userAnswer = userAnswers[currentQuestionIndex];
            if (userAnswer != null) {
                if (userAnswer == currentQuestion.isCorrectAnswer()) {
                    highlightCorrectAnswer(userAnswer);
                } else {
                    highlightWrongAnswer(userAnswer);
                }
            }
        // Verificar si ya fue respondida
        if (answeredQuestions[currentQuestionIndex]) {
            disableAnswerButtons();
        } else {
            enableAnswerButtons();
        }

        // Actualizar botones de navegación
        updateNavigationButtons();
    }

    private void checkAnswer(boolean userAnswer) {
        Question currentQuestion = questionList.get(currentQuestionIndex);

        // Marcar como respondida y guardar la respuesta
        answeredQuestions[currentQuestionIndex] = true;
        userAnswers[currentQuestionIndex] = userAnswer;

        // Verificar respuesta y resaltar botones
        if (userAnswer == currentQuestion.isCorrectAnswer()) {
            highlightCorrectAnswer(userAnswer);
        } else {
            highlightWrongAnswer(userAnswer);
        }

        // Deshabilitar botones de respuesta
        disableAnswerButtons();
    }


    private void highlightCorrectAnswer(boolean userAnswer) {
        Button correctButton = userAnswer ? btnTrue : btnFalse;
        Button otherButton = userAnswer ? btnFalse : btnTrue;

        // Colorear el botón correcto en verde
        correctButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));

        // Restaurar el color original del otro botón
        if (userAnswer) {
            otherButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        } else {
            otherButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        }
    }

    private void highlightWrongAnswer(boolean userAnswer) {
        Button wrongButton = userAnswer ? btnTrue : btnFalse;
        Button correctButton = userAnswer ? btnFalse : btnTrue;

        wrongButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        correctButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
    }

    private void enableAnswerButtons() {
        btnTrue.setEnabled(true);
        btnFalse.setEnabled(true);

        // Colores: Azul para Verdadero, Naranja para Falso
        btnTrue.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        btnFalse.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
    }

    private void disableAnswerButtons() {
        btnTrue.setEnabled(false);
        btnFalse.setEnabled(false);
    }

    private void updateNavigationButtons() {
        // Los botones siempre están habilitados porque funcionan en bucle
        btnPrevious.setEnabled(true);
        btnNext.setEnabled(true);
    }

    private void updateAnsweredCounter() {
        // Contar cuántas preguntas han sido respondidas correctamente
        int correctCount = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] != null) {
                // Verificar si la respuesta del usuario es correcta
                if (userAnswers[i] == questionList.get(i).isCorrectAnswer()) {
                    correctCount++;
                }
            }
        }

        // Actualizar el TextView con el contador de correctas
        answeredCounterTextView.setText(getString(R.string.answered_counter,
                                                   correctCount,
                                                   questionList.size()));
    }

    private void updateIncorrectCounter() {
        // Contar cuántas preguntas han sido respondidas incorrectamente
        int incorrectCount = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] != null) {
                // Verificar si la respuesta del usuario es incorrecta
                if (userAnswers[i] != questionList.get(i).isCorrectAnswer()) {
                    incorrectCount++;
                }
            }
        }

        // Actualizar el TextView con el contador de incorrectas
        incorrectCounterTextView.setText(getString(R.string.incorrect_counter,
                                                    incorrectCount,
                                                    questionList.size()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar el índice de la pregunta actual
        outState.putInt("currentQuestionIndex", currentQuestionIndex);

        // Guardar el array de preguntas respondidas
        outState.putBooleanArray("answeredQuestions", answeredQuestions);

        // Convertir Boolean[] a boolean[] para guardar las respuestas
        boolean[] tempAnswers = new boolean[userAnswers.length];
        boolean[] tempAnswersSet = new boolean[userAnswers.length];

        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] != null) {
                tempAnswers[i] = userAnswers[i];
                tempAnswersSet[i] = true;
            } else {
                tempAnswers[i] = false;
                tempAnswersSet[i] = false;
            }
        }

        outState.putBooleanArray("userAnswersValues", tempAnswers);
        outState.putBooleanArray("userAnswersSet", tempAnswersSet);
    }
}
















