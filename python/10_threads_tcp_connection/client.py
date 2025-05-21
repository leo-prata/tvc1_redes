import socket

t_host = "127.0.0.1"
t_port = 9998

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((t_host, t_port))

client.send(b"heeyyyyyyyyy")
response = client.recv(4096)
print(response)
client.close()