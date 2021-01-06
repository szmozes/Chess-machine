package machine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static machine.Color.BLACK;

public class View extends JPanel {
    Graphics g;
    int size;               // length of a field in pixels
    int menuWidth;          // menu width (at the right of the board)
    MenuItems rightMenu;    // menu at the right of the chessboard
    MenuHandler handleMenu; // execute the clicked menu item
    Controller controller;
    Table table;
    Color machineColor;
    boolean[][] opportunities;

    BufferedImage blackBishopImage;
    BufferedImage blackKingImage;
    BufferedImage blackKnightImage;
    BufferedImage blackPawnImage;
    BufferedImage blackQueenImage;
    BufferedImage blackRookImage;
    BufferedImage whiteBishopImage;
    BufferedImage whiteKingImage;
    BufferedImage whiteKnightImage;
    BufferedImage whitePawnImage;
    BufferedImage whiteQueenImage;
    BufferedImage whiteRookImage;

    public View() {
        rightMenu = new MenuItems();
        handleMenu = new MenuHandler();
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }

    private JMenuBar createMenu() {

        // menu bar
        JMenuBar menuBar = new JMenuBar();

        // first menu
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);

        // first menu items
        JMenuItem menuItem = new JMenuItem("New Game");
        menuItem.addActionListener(arg0 -> newGame());
        menu.add(menuItem);

        return menuBar;
    }

    public void init() {
        String imagePath = "images";

        try {
            blackBishopImage = ImageIO.read(new File(imagePath, "black_bishop.png"));
            blackKingImage = ImageIO.read(new File(imagePath, "black_king.png"));
            blackKnightImage = ImageIO.read(new File(imagePath, "black_knight.png"));
            blackPawnImage = ImageIO.read(new File(imagePath, "black_pawn.png"));
            blackQueenImage = ImageIO.read(new File(imagePath, "black_queen.png"));
            blackRookImage = ImageIO.read(new File(imagePath, "black_rook.png"));
            whiteBishopImage = ImageIO.read(new File(imagePath, "white_bishop.png"));
            whiteKingImage = ImageIO.read(new File(imagePath, "white_king.png"));
            whiteKnightImage = ImageIO.read(new File(imagePath, "white_knight.png"));
            whitePawnImage = ImageIO.read(new File(imagePath, "white_pawn.png"));
            whiteQueenImage = ImageIO.read(new File(imagePath, "white_queen.png"));
            whiteRookImage = ImageIO.read(new File(imagePath, "white_rook.png"));
        } catch (IOException e) {
            System.out.println("some of the pictures couldn't be loaded");
        }

        size = 60;
        menuWidth = rightMenu.getMenuWidth(size);
        Game game = new StandardGameAgainstMachine(BLACK);
        controller = new Controller(this, game);
        controller.game = game;
        table = controller.game.table;
        controller.gameType = GameType.AGAINST_MACHINE;
        machineColor = BLACK;
        addMouseListener(controller);
        setPreferredSize(new Dimension(8 * size + menuWidth, 8 * size));
        opportunities = new boolean[8][8];
    }

    public PieceKind askUserForPiece() {
        Object[] pieceEnums = {PieceKind.QUEEN, PieceKind.BISHOP, PieceKind.KNIGHT, PieceKind.ROOK};
        PieceKind chosenPieceKind = (PieceKind) JOptionPane.showInputDialog(null,
                "Choose the piece you want", "Choosing piece",
                JOptionPane.QUESTION_MESSAGE, null, pieceEnums, pieceEnums[0]);
        if (chosenPieceKind == null) {
            chosenPieceKind = PieceKind.QUEEN;
        }
        return chosenPieceKind;
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.g = g;

        // recalculate the current size of a chessboard square
        size = rightMenu.getSquareSize(this.getWidth(), this.getHeight());
        rightMenu.paint(g);

        // background
        for (int i = 0; i < table.height; i++) {
            for (int j = 0; j < table.width; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(new java.awt.Color(255, 255, 127));
                } else {
                    g.setColor(new java.awt.Color(127, 63, 0));
                }
                g.fillRect(j * size, i * size, size, size);
            }
        }
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(0, 0, size * table.width - 1, size * table.height - 1);

        // pieces
        for (int i = 0; i < table.height; i++) {
            for (int j = 0; j < table.width; j++) {
                if (table.fields[i][j].getPiece() != null) {
                    BufferedImage pieceImage = getImageByPiece(table.fields[i][j].piece);
                    this.g.drawImage(pieceImage, j * size, i * size, size, size, null);
                }
            }
        }

        // opportunity signs
        for (int i = 0; i < opportunities.length; i++){
            for (int j = 0; j < opportunities[i].length; j++){
                if (opportunities[i][j]) {
                    this.g.setColor(new java.awt.Color(200, 200, 255, 255));
                    this.g.fillOval(j * size + size * 3 / 8, i * size + size * 3 / 8, size / 4, size / 4);
                    this.g.setColor(new java.awt.Color(0, 0, 0, 255));
                    this.g.drawOval(j * size + size * 3 / 8, i * size + size * 3 / 8, size / 4, size / 4);
                }
            }
        }
    }

    private static void createAndShowGUI() {
        View view = new View();
        view.init();

        JFrame f = new JFrame("Chess Program");
        f.setJMenuBar(view.createMenu());
        f.setContentPane(view);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void newGame() {
        Object[] colors = {BLACK, Color.WHITE};
        Color chosenColor = (Color) JOptionPane.showInputDialog(null,
                "Choose color you want to play with", "Choosing color",
                JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);
        if (chosenColor == null) {
            chosenColor = Color.WHITE;
        }
        if (controller.gameType == GameType.BOTH_USER) {
            controller.game = new StandardGameAgainstMachine(Color.values()[1 - chosenColor.ordinal()]);
        }
    }

    private BufferedImage getRead(String imagePath, String filename) {
        try {
            return ImageIO.read(new File(imagePath, filename));
        } catch (IOException e) {
            System.out.println(filename + " at " + imagePath + " couldn't be loaded");
            return null;
        }
    }

    private BufferedImage getImageByPiece(Piece piece) {
        if (piece.kind == PieceKind.BISHOP) {
            return piece.color == BLACK ? blackBishopImage : whiteBishopImage;
        } else if (piece.kind == PieceKind.KING) {
            return piece.color == BLACK ? blackKingImage : whiteKingImage;
        } else if (piece.kind == PieceKind.KNIGHT) {
            return piece.color == BLACK ? blackKnightImage : whiteKnightImage;
        } else if (piece.kind == PieceKind.PAWN) {
            return piece.color == BLACK ? blackPawnImage : whitePawnImage;
        } else if (piece.kind == PieceKind.QUEEN) {
            return piece.color == BLACK ? blackQueenImage : whiteQueenImage;
        } else if (piece.kind == PieceKind.ROOK) {
            return piece.color == BLACK ? blackRookImage : whiteRookImage;
        } else {
            return null;
        }
    }
}