import socket
import threading

IP = "0.0.0.0"
PORT = 9998
MAX_THREADS = 10

threads_busy = [False] * MAX_THREADS
lock = threading.Lock()

def handle_client(client_socket):
    with client_socket as sock:
        request = sock.recv(1024)
        print(f"[*] Received: {request.decode('utf-8')}")
        sock.send(request)

def worker_thread(thread_id):
    global threads_busy
    while True:
        with lock:
            if not hasattr(worker_thread, 'client_queue'):
                worker_thread.client_queue = []
            if worker_thread.client_queue:
                client = worker_thread.client_queue.pop(0)
                threads_busy[thread_id] = True
            else:
                threads_busy[thread_id] = False
                continue
        
        handle_client(client)
        threads_busy[thread_id] = False

def main():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((IP, PORT))
    server.listen(5)
    print(f"[*] Listening on {IP}:{PORT}")

    for i in range(MAX_THREADS):
        t = threading.Thread(target=worker_thread, args=(i,))
        t.daemon = True
        t.start()

    try:
        while True:
            client, addr = server.accept()
            print(f"[*] Accepted connection from {addr[0]}:{addr[1]}")
            
            with lock:
                if any(not busy for busy in threads_busy):
                    if not hasattr(worker_thread, 'client_queue'):
                        worker_thread.client_queue = []
                    worker_thread.client_queue.append(client)
                else:
                    print(f"[!] All threads busy, rejecting connection from {addr[0]}:{addr[1]}")
                    client.send(b"Server busy. Please try again later.")
                    client.close()
    except KeyboardInterrupt:
        print("\n[*] Shutting down server...")
    finally:
        server.close()

if __name__ == '__main__':
    main()