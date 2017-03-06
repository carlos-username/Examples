#!/usr/bin/python2
import networkx as nx
import matplotlib.pyplot as plt
import operator

def draw_graph(graph, labels=None,node_color='blue',
               node_size=1600, graph_layout='shell' , node_alpha=0.3,
               node_text_size=12,
               edge_color='blue', edge_alpha=0.3, edge_tickness=1,
               edge_text_pos=0.3,
               text_font='sans-serif'):

    # create networkx graph
    G=nx.Graph()

    # add edges
    for edge in graph:
        G.add_edge(edge[0], edge[1])
    
    # these are different layouts for the network you may try
    # shell seems to work best
    if graph_layout == 'spring':
        graph_pos=nx.spring_layout(G)
    elif graph_layout == 'spectral':
        graph_pos=nx.spectral_layout(G)
    elif graph_layout == 'random':
        graph_pos=nx.random_layout(G)
    else:
        graph_pos=nx.shell_layout(G)

    nx.draw_networkx_nodes(G,graph_pos,node_size=node_size,
                           alpha=node_alpha,node_color=node_color)
    
    nx.draw_networkx_edges(G,graph_pos,width=edge_tickness,
                           alpha=edge_alpha,edge_color=edge_color)
    nx.draw_networkx_labels(G, graph_pos,font_size=node_text_size,
                            font_family=text_font)

    if labels is None:
        labels = range(len(graph))

    edge_labels = dict(zip(graph, labels))
    nx.draw_networkx_edge_labels(G, graph_pos, edge_labels=edge_labels, 
                                 label_pos=edge_text_pos)
    grafito={}
    # show graph
    for conta in xrange(len(graph)):
        grafito[graph[conta][0]]=G.degree(graph[conta][0])
    grafito = list(grafito.items())
    return grafito

def welch(grafo,nodos_valencia):
    g=[]
    col=[]
    colores={}
    colores[nodos_valencia[0]]=1
    conta_color=1
    col.append(nodos_valencia[0])
    while True:
        g=[]
        for value in xrange(1,len(nodos_valencia)):
            bandera=False
            for val in xrange(len(col)):
                for valor in grafo[col[val]]:
                    if nodos_valencia[value]==valor: 
                        bandera=True
                        break
            if not bandera:
                colores[nodos_valencia[value]]=conta_color
                col.append(nodos_valencia[value])
            else:
                g.append(nodos_valencia[value])
        nodos_valencia=g
        print "coloreado_actual->",col
        print "siguiente->",g
        col=[]
        if len(nodos_valencia)>0:
            conta_color+=1
            col.append(nodos_valencia[0])
            colores[nodos_valencia[0]]=conta_color
        if not len(g):
            break
            
    print "colores->",colores
    print "grafo->",grafo
    lista_colores=[]
    for i in colores:
        lista_colores.append(colores[i])
    print colores
    return lista_colores

def graph_drawing():
   
    l = []
    with open('adjacency.txt', 'r') as f:
        for line in f:
            line = line.strip()
            if len(line) > 0:
                l.append(map(int, line.split(' ')))
    plt.show()
    a=[]
    b={}
    #print a
    for i in xrange(len(l)):
        a=[]
        for j in xrange(len(l)):
            if l[i][j]:
                a.append(j)
        
        b[i]=a
    tupla_nuevo=[]
    #for i in b:
    #    for j in b[i]:
     #       tupla_nuevo.append((i,j))
    for i in xrange(len(l)):
        for j in xrange(len(l)):
            if l[i][j]:
                tupla_nuevo.append((i,j))
    labels = map(chr, range(65, 65+len(tupla_nuevo)))
    plt.clf()
    grados=draw_graph(tupla_nuevo,labels)
    grados=sorted(grados, reverse=True, key=operator.itemgetter(1))
    print grados
    nodos_valencia=[]
    for i in xrange(len(grados)):
        nodos_valencia.append(grados[i][0])
    print "nodos_valencia->",nodos_valencia
    print l
    print b
    print tupla_nuevo
    lista_solucion=welch(b,nodos_valencia)
    f=open("solucion.txt","w")
    for i in lista_solucion:
        #print i,
        #f.write(i+",")
        f.write ("%s%s" % (i,","))
    f.write("\n")
    f.close()
    grados=draw_graph(tupla_nuevo,labels,node_color=lista_solucion)
    plt.savefig("grafica.jpg") # se guardan las imagenes ".png" de cada una de las graficas generadas por iteracion
    plt.show()

graph_drawing()   

    
