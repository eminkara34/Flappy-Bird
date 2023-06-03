package flappyBird;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
        private BufferedImage image;
        private int basmaSayısı=0;
        private int süre=0;
        private int time=0;
	public static FlappyBird flappyBird;
	public final int WIDTH = 800, HEIGHT = 800;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	public int ticks, yMotion, score;
	public boolean gameOver, started;
	public Random rand;
	public FlappyBird()
	{
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		renderer = new Renderer();
		rand = new Random();
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		timer.start();
                try {            
           
            image=ImageIO.read(new FileImageInputStream(new File("fl.jpg"))); 
            
            
        } catch (IOException ex) {
            Logger.getLogger(FlappyBird.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void addColumn(boolean start)
	{
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		if (start)
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.gray.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	public void jump()
	{
		if (gameOver)
		{
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			gameOver = false;
		}
		if (!started)
		{
			started = true;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 11;
		}
	}
	public void actionPerformed(ActionEvent e)
	{
               
		int speed = 10;
		ticks++;
		if (started)
		{
			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);
				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);

					if (column.y == 0)
					{
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;
			for (Rectangle column : columns)
			{
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
				{
					score++;
				}

				if (column.intersects(bird))
				{
					gameOver = true;

					if (bird.x <= column.x)
					{
						bird.x = column.x - bird.width;

					}
					else
					{
						if (column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height)
						{
							bird.y = column.height;
						}
					}
				}
			}
			if (bird.y > HEIGHT - 120 || bird.y < 0)
			{
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}
		renderer.repaint();
	}

	public void repaint(Graphics g)
	{
                süre+=5;
                 if (süre%500==0)
                  {
                    time+=1;
                  }
               
               
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);
		g.setColor(Color.cyan);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
		g.setColor(Color.WHITE);		
                //g.drawImage(image, bird.x,bird.y, bird.width, bird.height, (ImageObserver) this);
                g.drawImage(image,bird.x,bird.y, 50, 40, renderer);
		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}
                g.setColor(Color.ORANGE);
                g.setFont(new Font("arial", Font.ITALIC, 30));   
                g.drawString("MOVE :"+"  "+basmaSayısı, 50, 30);
                g.setColor(Color.RED);
                g.setFont(new Font("arial", Font.ITALIC, 30));
                g.drawString("TIME :"+"  "+time, 300, 30);
                
                
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 1, 50));
		if (!started)
		{
			g.drawString("Click to start the game", 100, HEIGHT / 2 - 50);
                       
		}

		if (gameOver)
		{
			g.drawString("Try again!", 125, HEIGHT / 2 - 50);
                        basmaSayısı=0;
                        time=0;
                        
		}

		if (!gameOver && started)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args)
	{
		flappyBird = new FlappyBird();
	}
	public void mouseClicked(MouseEvent e)
	{
		jump();
                basmaSayısı++;
	}
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
                        basmaSayısı++;
		}
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void keyPressed(KeyEvent e)
	{
	}
}