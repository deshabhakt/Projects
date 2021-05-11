#include <iostream>
using namespace std;

template <class T>
class SLL
{
    template <class X>
    class Node
    {
        X data;
        Node<X> *next;

    public:
        Node()
        {
            next = NULL;
            data = 0;
        }
        void setData(X d)
        {
            data = d;
        }
        X getData()
        {
            return data;
        }
        Node<X> *getNext()
        {
            return next;
        }
        void setNext(Node<X> *ptr2)
        {
            this->next = ptr2;
        }
    };
    Node<T>* head;
public:
    SLL()
    {
        head = NULL;
    }
    SLL(T d)
    {
        head = new Node<T>();
        head->setData(d);
    }
    void AppendNode(T d)
    {
        Node<T> *ptr = new Node<T>();
        ptr->setData(d);
        if (head == NULL)
        {
            head = ptr;
        }
        else
        {
            Node<T> *prev;
            Node<T> *temp = head;
            while (temp != NULL)
            {
                prev = temp;
                temp = temp->getNext();
            }
            prev->setNext(ptr);
        }
    }
    void prependNode(T d)
    {
        Node<T> *ptr = new Node<T>();
        ptr->setData(d);
        ptr->setNext(head);
        head = ptr;
    }

    void insertNodeAfter(T elementBeforeTheElementTobeInserted, T tobeInserted)
    {
        Node<T> *ptr = new Node<T>();
        ptr->setData(tobeInserted);
        Node<T> *temp = head;
        T gdata = temp->getData();
        while (gdata != elementBeforeTheElementTobeInserted)
        {
            temp = temp->getNext();
            gdata = temp->getData();
        }
        if (temp == NULL)
        {
            cout << "No Element Found" << endl;
        }
        else
        {
            ptr->setNext(temp->getNext());
            temp->setNext(ptr);
        }
    }

    void deleteNode(T d)
    {
        Node<T> *ptr = new Node<T>();
        ptr->setData(d);
        if (head == NULL)
        {
            cout << "List is Empty" << endl;
        }
        else
        {

            if (d == head->getData())
            {
                head = head->getNext();
            }
            else
            {
                Node<T> *prev = head;
                Node<T> *currptr = head->getNext();
                while (d != currptr->getData() && prev != NULL)
                {
                    prev = currptr;
                    currptr = currptr->getNext();
                    if (currptr == NULL)
                    {
                        cout << "Node Not found" << endl;
                        break;
                    }
                }
                if (currptr != NULL)
                    prev->setNext(currptr->getNext());
            }
        }
    }
    void printSLL()
    {
        Node<T> *ptr = new Node<T>();
        ptr = head;
        while (ptr != NULL)
        {
            T d = ptr->getData();
            cout << d << "      ";
            ptr = ptr->getNext();
        }
        cout << endl;
    }

    ~SLL()
    {
        Node<T> *temp = head;
        while (temp != NULL)
        {
            Node<T> *next = temp->getNext();
            delete temp;
            temp = next;
        }
        delete temp;
    }
};
int main()
{
    SLL<int> sll;
    sll.AppendNode(2);
    sll.prependNode(1);
    sll.AppendNode(4);
    sll.insertNodeAfter(2, 3);
    sll.printSLL();

    sll.deleteNode(4);
    sll.printSLL();
    sll.deleteNode(1);
    sll.printSLL();
}