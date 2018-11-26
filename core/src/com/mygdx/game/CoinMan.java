package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.omg.PortableInterceptor.Interceptor;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	int pause=0;
	Texture[] man;
	int manState=0;
	int manY=0;
	float gravity=0.2f;
	float velocity=0;

	Random random;
	ArrayList<Integer> coinXs=new ArrayList<Integer>();
	ArrayList<Integer> coinYs=new ArrayList<Integer>();
	ArrayList<Integer> bombXs=new ArrayList<Integer>();
	ArrayList<Integer> bombYs=new ArrayList<Integer>();
	int countCoins;
	int countBombs;
	Texture coin;
	Texture bomb;
	int gameState=0;
	BitmapFont font;
	int score;
	ArrayList<Rectangle> coinRectangle=new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<Rectangle>();
	Rectangle manRectangle;
	Texture dizzy;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
        man[2]=new Texture("frame-3.png");
        man[3]=new Texture("frame-4.png");
        bomb=new Texture("bomb.png");
        random=new Random();
        coin=new Texture("coin.png");
        manY=Gdx.graphics.getHeight()/2;
        dizzy=new Texture("dizzy-1.png");
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
	}

	public void makeCoin(){
		coinXs.add(Gdx.graphics.getWidth());
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
	}

	public void makebomb(){
		float height=random.nextFloat() * Gdx.graphics.getHeight();
		bombXs.add(Gdx.graphics.getWidth());
		bombYs.add((int)height);
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState==1){
			//Game is live

			if(countBombs<250){
				countBombs++;
			}else {
				countBombs=0;
				makebomb();
			}

			bombRectangle.clear();
			for (int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-8);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));

			}


			if (countCoins<100){
				countCoins++;
			}else {
				countCoins=0;
				makeCoin();
			}
			coinRectangle.clear();
			for(int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}


			if(Gdx.input.justTouched()){
				velocity=-10;
			}

			if(pause<8){
				pause++;
			}else {
				pause=0;
				if(manState<3){
					manState++;
				}else {
					manState=0;
				}
			}

			velocity+=gravity;
			manY-=velocity;
			if(manY<=0){
				manY=0;
			}
		}else if (gameState==0){
			//waiting to start
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if (gameState==2){
			if(Gdx.input.justTouched()){
				gameState=1;
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				countCoins=0;
				coinYs.clear();
				coinXs.clear();
				coinRectangle.clear();
				countBombs=0;
				bombYs.clear();
				bombXs.clear();
				bombRectangle.clear();
			}
		}

		if(gameState==2){
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}else {

			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
        manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
        for (int i=0;i<coinRectangle.size();i++){
        	if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
        		//Gdx.app.log("Coins","Collision!!!");
				score++;
				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				Gdx.app.log("Bombs","Collision!!!");
				gameState=2;
			}
		}


		font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
