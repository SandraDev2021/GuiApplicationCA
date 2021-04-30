import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jmdns.ServiceInfo;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import BidirectionalCA.ReferenceLetterGrpc;
import BidirectionalCA.ReferenceLetterRequest;
import BidirectionalCA.ReferenceLetterResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import jmDNSBiDi.SimpleServiceDiscovery;


public class GuiApp implements ActionListener{
		
		private JTextArea textReponse;
		private JFrame frame;
		
		//CustomerSupport
		private JPanel getCustomerServiceJPanel() {
			
			  	JPanel panel = new JPanel();
		        JButton button = new JButton("Invoke CustomerSupport");
		        button.addActionListener(this);
		        panel.add(button);
		        return panel;

			
		}
		//CustomerSupport1
		   private JPanel getCustomerSupport1JPanel() {


		        JPanel panel = new JPanel();
		        JButton button = new JButton("Invoke CustomerSuport1");
		        button.addActionListener(this);
		        panel.add(button, BorderLayout.NORTH);



		        return panel;


		    }
		 //invoke Certificate
		   private JPanel getCertificateJPanel() {

		        JPanel panel = new JPanel();
		        JButton button = new JButton("Invoke Certificate");
		        button.addActionListener(this);
		        panel.add(button);
		        return panel;


		    }
		   //invoke ReferenceLetter
		    private JPanel getReferenceLetterJPanel() {


		        JPanel panel = new JPanel();
		        JButton button = new JButton("Invoke ReferenceLetter");
		        button.addActionListener(this);
		        panel.add(button);
		        return panel;


		    } 
		
		    public static void main(String[] args){

		        GuiApp gui = new GuiApp();
		        gui.build();

		    }

		private void build() {
				// TODO Auto-generated method stub
			 //create the title
	        JFrame frame = new JFrame("Automated Bussiness Process App");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        // Set the panel to add buttons
	        JPanel panel = new JPanel();

	        // Set the BoxLayout to be X_AXIS: from left to right
	        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

	        panel.setLayout(boxlayout);

	        // Set border for the panel
	        panel.setBorder(new EmptyBorder(new Insets(50, 100, 50, 100)));
	        //add the panels
	        panel.add( getCustomerServiceJPanel() );
	     //   panel.add( getCustomerSupport1JPanel() );
	      //  panel.add( getCertificateJPanel() );
	     //   panel.add( getReferenceLetterJPanel() );

	        // Set size for the frame
	        frame.setSize(300, 300);
	        textReponse = new JTextArea(10, 40);
	        textReponse .setLineWrap(true);
	        textReponse.setWrapStyleWord(true);
	        JScrollPane scrollPane = new JScrollPane(textReponse);
	        //textResponse.setSize(new Dimension(15, 30));
	        panel.add(scrollPane);
	        panel.setLayout(boxlayout);

	        // Set the window to be visible as the default to be false
	        frame.add(panel);
	        frame.pack();
	        frame.setVisible(true);	
			
			
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			 JButton button = (JButton)e.getSource();
		     String label = button.getActionCommand();
		        
		         //Invocation of Unary Server
		         
			if(label.equals("Invoke CustomerSupport")) {
				System.out.println(" service Customer Support :");
				//code from client unary
				
				ServiceInfo serviceInfo;
				String service_type = "_grpc._tcp.local.";
				//Now retrieve the service info - all we are supplying is the service type
				serviceInfo = SimpleServiceDiscovery.run(service_type);
				//Use the serviceInfo to retrieve the port
				int port = 50051;//serviceInfo.getPort();
				String host = "localhost";
				//int port = 50051;
				
			// we build a channel to establish connection with the server. For address we use a localhost and the port	
				ManagedChannel channel = ManagedChannelBuilder.
						forAddress(host, port)
						.usePlaintext()
						.build();
				

				
				System.out.println("Creating stub");
				// creating stub using a channel
				CustomerSupportGrpc.CustomerSupportBlockingStub myclient = CustomerSupportGrpc.newBlockingStub(channel);
				
				
			    try {
			    	// here, goes your code
			    	
					// create the request from the client
					SupportRequest request = SupportRequest.newBuilder().setPayment("How can I pay for the course?").build();
					
					//create the response
					SupportResponse response = myclient.getCustomerSupport(request);
					
					
					System.out.println(response.getCardPay());// the response is "the course can be paid by transference or by card"
					
					System.out.println("Shutting down channel");
					channel.shutdown();// finish the connection after the service is provided.
			    	 
			    	 //logger.info("Greeting: " + response.getMessage());
			    	 
			    } catch (StatusRuntimeException ex) {
				   System.out.println(ex.getStatus());
				    
				    return;		
				    
			    } finally {
			    	//shutdown channel
			    	try {
						channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
			  }
			
			else if(label.equals("Invoke CustomerSuport1")) {
				System.out.println(" service Customer Support1 :");
				
				//code from client server streaming
				
				//ServiceInfo serviceInfo;
				String service_type = "_CustomerSupport._tcp.local.";
				
				//now retrieve the service information we are supplying in service_type
				ServiceInfo serviceInfo;
				
				serviceInfo = SimpleServiceDiscovery.run(service_type);
				int port = 50051;//serviceInfo.getPort();
				String host = "localhost";
				//int port = 50051;
	
				ManagedChannel channel = ManagedChannelBuilder.
						forAddress(host, port)
						.usePlaintext()
						.build();
				 
				CustomerSupportGrpc.CustomerSupportBlockingStub myclient = CustomerSupportGrpc.newBlockingStub(channel);
				
				
				try {
					//SERVER STREAM
					//create the client's request - just one
					SupportRequest1 available_day = SupportRequest1.newBuilder().setAvailableDay("What are the course's available days?").build();
					
					//stream the responses (in a blocking manner)
					 myclient.getCustomerSupport1(available_day).forEachRemaining(SupportResponse1 ->{
						System.out.println(SupportResponse1.getCourseStartJune14()+ SupportResponse1.getCourseStartJune28()
						+ SupportResponse1.getCourseStartAugust16()
								
								);
					});
				}catch(StatusRuntimeException ex) {
				   System.out.println(ex.getStatus());
				    
				    return;		
				    
			    } finally {
			    	//shutdown channel
			    	System.out.println("Shutting down channel");
			    	try {
						channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
			  }
					
				
			else if(label.equals("Invoke Certificate")) {
				System.out.println(" service Certificate :");
				
				// code from client client streaming
				
			}else if(label.equals("Invoke ReferenceLetter")) {
				System.out.println(" service Certificate :");
				
				// code from client bidi
			
					ServiceInfo serviceInfo;
					String service_type = "_grpc2_.tcp.local.";
					serviceInfo = SimpleServiceDiscovery.run(service_type);
					int port =50051; //serviceInfo.getPort();
					String host = "localhost";
					
					ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
							.usePlaintext()
							.build();
		
					ReferenceLetterGrpc.ReferenceLetterStub asyncBiDi = ReferenceLetterGrpc.newStub(channel);
					
					// create the latch - instantiate it
					CountDownLatch latch = new CountDownLatch(1);
				
					// creating the requestObserver using the asynchronous client object
					StreamObserver<ReferenceLetterRequest> requestObserver = asyncBiDi.getReferenceLetter(new StreamObserver<ReferenceLetterResponse>() {
							
						
						@Override
						public void onNext(ReferenceLetterResponse value) {
							// Every time the client get a response from the server
							//System.out.println("Response from the server:" + value.getConfirmation());
							
						}

						@Override
						public void onError(Throwable t) {
							latch.countDown();
							
						}

						@Override
						public void onCompleted() {
							System.out.println("Server is done sending data");
							latch.countDown();
							
						}
						
					});
					try {	
						
						requestObserver.onNext(ReferenceLetterRequest.newBuilder().setCourse("I would like to get a reference letter regarding the Door Security course").build());
						requestObserver.onNext(ReferenceLetterRequest.newBuilder().setLanguage("in English").build());// this construction is not giving me an output
							
						
						requestObserver.onCompleted();// when the client is done with the requests
						
					Thread.sleep(1000);//wait 0,1 seconds for the next
						
						latch.await(3L,TimeUnit.SECONDS);
						
					}catch(InterruptedException ex) {
						ex.printStackTrace();
					}

			}

			
		
		}//end of actionPerformed		
	}// end of class GuiApplication
