package com.example.victo.atjohn;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {


    InterstitialAd mInterstitialAd;
    Button botaoAd, btValidar;
    EditText etNome, etEmail, etSenha, etConfirmarSenha, etCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoAd = (Button) findViewById(R.id.btnAd);
        btValidar = (Button) findViewById(R.id.btValidar);
        etNome = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etSenha = (EditText) findViewById(R.id.etSenha);
        etConfirmarSenha = (EditText) findViewById(R.id.etConfirmarSenha);
        etCpf = (EditText) findViewById(R.id.etCpf);


        //criando mascara para o cpf
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(etCpf, smf);
        etCpf.addTextChangedListener(mtw);


        //admob banner
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        //chave do admob
        mInterstitialAd.setAdUnitId("ca-app-pub-2048265031968487/7982707743");

        //metodo do botao ad
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Toast.makeText(MainActivity.this, " Obrigado !", Toast.LENGTH_SHORT).show();
               //limpar os campos edit text
                etNome.setText("");
                etEmail.setText("");
                etSenha.setText("");
                etConfirmarSenha.setText("");
                etCpf.setText("");

                //tornando não clicavel até que os campos sejam validados
                botaoAd.setEnabled(false);
            }
        });


        requestNewInterstitial();

        //botao ad
        botaoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarTxt();
                mInterstitialAd.show();

            }
        });
    }

    //requisição Interstitial anuncio
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
              /*  .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")*/
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    //validando campos
    public void validar(View view) {

        EditText editar_nome = (EditText) findViewById(R.id.etNome);
        EditText editar_email = (EditText) findViewById(R.id.etEmail);
        EditText editar_senha = (EditText) findViewById(R.id.etSenha);
        EditText editar_confirmarsenha = (EditText) findViewById(R.id.etConfirmarSenha);
        EditText editar_cpf = (EditText) findViewById(R.id.etCpf);

        String nomeTxt = editar_nome.getText().toString();
        String emailTxt = editar_email.getText().toString();
        String senhaTxt = editar_senha.getText().toString();
        String confirmarSenhaTxt = editar_confirmarsenha.getText().toString();
        String cpfTxt = editar_cpf.getText().toString();

        if (nomeTxt == null || nomeTxt.equals("")) {
            editar_nome.setError("Este campo é obrigatório preencher");
        }
        else if(emailTxt == null || emailTxt.equals("") || !isEmailValido(etEmail.getText().toString())){
            editar_email.setError("Este campo é obrigatório preencher");
        }
        else if(senhaTxt == null || senhaTxt.equals("")){
            editar_senha.setError("Este campo é obrigatório preencher");
        }
        else if(confirmarSenhaTxt == null || confirmarSenhaTxt.equals("") || !validarSenha(etSenha.getText().toString(), etConfirmarSenha.getText().toString()) ){
            editar_confirmarsenha.setError("Este campo é obrigatório preencher");
        }
        else if(cpfTxt == null || cpfTxt.equals("") || cpfTxt.length()==11){
            editar_cpf.setError("Este campo é obrigatório preencher");
        }
        else {
            Toast.makeText(this, "Preenchido corretamente!", Toast.LENGTH_SHORT).show();
            botaoAd.setEnabled(true);
        }


    }

    //salvando em txt
    public void SalvarTxt (){

        Contato contato = new Contato(etNome.getText().toString(),etEmail.getText().toString(),etSenha.getText().toString(),etConfirmarSenha.getText().toString(),etCpf.getText().toString());

        try{
            FileOutputStream fos = openFileOutput("contatos.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(contato);

            Mensagem("Contato salvo com sucesso!");


        }catch (Exception e){

            Mensagem("Erro : " + e.getMessage());

        }


    }

    //escrevendo uma msg
    private void Mensagem (String msg){

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }

    //validando email
    private boolean isEmailValido(String email){

        boolean resultado = (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    // validando senha
    private boolean validarSenha(String senha1 , String senha2){
        if(senha1.equals(senha2)){
            return true;
        }else{
            return false;
        }
    }

}


