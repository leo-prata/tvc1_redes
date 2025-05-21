import socket

t_host = "127.0.0.1"
t_port = 9998

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) # standard ipv4 adress // tcp client

client.connect((t_host, t_port))

client.send(b"heeyyyyyyyyy") # data as bytes

response = client.recv(4096) # max amount of data to be received

print(response)
client.close()