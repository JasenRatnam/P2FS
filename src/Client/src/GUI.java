import Handler.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class GUI {
    public GUI() {
        JFrame jFrame = new JFrame("COEN 366 Client");
        jFrame.setSize(450, 450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("Client Manager");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlOperation = new JLabel("Choose an operation to perform");
        jlOperation.setFont(new Font("Arial", Font.BOLD, 20));
        jlOperation.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlOperation.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jbRegister = new JButton("Register");
        jbRegister.setPreferredSize(new Dimension(150, 75));
        jbRegister.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbDeregister = new JButton("Deregister");
        jbDeregister.setPreferredSize(new Dimension(150, 75));
        jbDeregister.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbPublish = new JButton("Publish");
        jbPublish.setPreferredSize(new Dimension(150, 75));
        jbPublish.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbRemove = new JButton("Remove");
        jbRemove.setPreferredSize(new Dimension(150, 75));
        jbRemove.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbRetrieveAll = new JButton("Retrieve All");
        jbRetrieveAll.setPreferredSize(new Dimension(150, 75));
        jbRetrieveAll.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbRetrieveinfoT = new JButton("Retrieve infoT");
        jbRetrieveinfoT.setPreferredSize(new Dimension(150, 75));
        jbRetrieveinfoT.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbSearchFile = new JButton("Search file");
        jbSearchFile.setPreferredSize(new Dimension(150, 75));
        jbSearchFile.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbDownload = new JButton("Download");
        jbDownload.setPreferredSize(new Dimension(150, 75));
        jbDownload.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbUpdatecontact = new JButton("Update contact");
        jbUpdatecontact.setPreferredSize(new Dimension(150, 75));
        jbUpdatecontact.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbDisconnect = new JButton("Disconnect");
        jbDisconnect.setPreferredSize(new Dimension(150, 75));
        jbDisconnect.setFont(new Font("Arial", Font.BOLD, 20));

        jpButton.add(jbRegister);
        jpButton.add(jbDeregister);
        jpButton.add(jbPublish);
        jpButton.add(jbRemove);
        jpButton.add(jbRetrieveAll);
        jpButton.add(jbRetrieveinfoT);
        jpButton.add(jbSearchFile);
        jpButton.add(jbDownload);
        jpButton.add(jbUpdatecontact);
        jpButton.add(jbDisconnect);

        jbRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.ui();
                Scanner sc = new Scanner(System.in);
                if(sc.equals(1)){
                    Client.register(sc);
                }else{
                    System.out.println("User is already registered");
                }

            }
        });

        jbDeregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner sc = new Scanner(System.in);
                Client.deregister(sc);
            }
        });

        jbPublish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner sc = new Scanner(System.in);
                Client.publish(sc);
            }
        });

        jbRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner sc = new Scanner(System.in);
                Client.remove(sc);
            }
        });

        jbRetrieveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.RetrieveAll();
            }
        });

        jbRetrieveinfoT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.RetrieveInfo();
            }
        });

        jbSearchFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner sc = new Scanner(System.in);
                Client.SearchFile(sc);
            }
        });

        jbDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.download();
            }
        });

        jbUpdatecontact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.update();
            }
        });

        jbDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        jFrame.add(jlTitle);
        jFrame.add(jlOperation);
        jFrame.add(jpButton);
        jFrame.setVisible(true);

    }
}
