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
    Node<T> *head;

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
    bool updateNode(T before, T after)
    {

        Node<T> *ptr = this->head;
        while (ptr->getData() != before && ptr != NULL)
        {
            ptr = ptr->getNext();
        }
        ptr->setData(after);
        if (ptr == NULL)
        {
            return false;
        }
        return true;
    }
    bool deleteNode(T d)
    {
        Node<T> *ptr = new Node<T>();
        ptr->setData(d);
        if (head == NULL)
        {
            return false;
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
                        return false;
                    }
                }
                if (currptr != NULL)
                    prev->setNext(currptr->getNext());
            }
            return true;
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

    void generalSwitchCaseBlock(SLL<T> sll)
    {
        T data;
        char varForSwitchCase;
        do
        {
        label:
            cout << "\n1.Append Node\n2.PrependNode\n3.Insert Node after a particular node\n4.Delete Node\n5.Update Node data\n6.Print singly linked list\n7.Clear Screen\n8.Go to previous menu\n> ";
            cin >> varForSwitchCase;
            if (!isdigit(varForSwitchCase))
            {
                cout << "Invalid input" << endl;
                goto label;
            }

            switch ((int)varForSwitchCase - '0')
            {
            case 1:
            {
                cout << "Append Node Operation\nEnter node to be appended:" << endl;
                cout << ">";
                cin >> data;
                sll.AppendNode(data);
                break;
            }
            case 2:
            {
                cout << "Prepend Node Operation\nEnter node to be prepended:" << endl;
                cout << ">";
                cin >> data;
                sll.prependNode(data);
                break;
            }
            case 3:
            {
                T node_after_which_new_node_to_be_inserted;
                cout << "Insert Node Operation" << endl;
                cout << "Enter node after which you want to insert a new node:\n>";
                cin >> node_after_which_new_node_to_be_inserted;
                cout << "Enter node to be inserted:\n>";
                cin >> data;
                sll.insertNodeAfter(node_after_which_new_node_to_be_inserted, data);
                break;
            }
            case 4:
            {
                cout << "Delete Node Operation (Deletion by key of node)" << endl;
                cout << "Enter node to be deleted:" << endl;
                cout << ">";
                cin >> data;
                if(sll.deleteNode(data)){
                    cout << "Node deleted successfully" << endl;
                }
                else{
                    cout << "Node not found" << endl;
                }
                break;
            }
            case 5:
            {
                T before;
                cout << "Update Node" << endl;
                cout << "Enter node which is to be updated: ";
                cin >> before;
                cout << "\nEnter new data: ";
                cin >> data;
                if (sll.updateNode(before, data))
                {
                    cout << "Node updated successfully" << endl;
                }
                else
                {
                    cout << "Given node not found" << endl;
                }
                break;
            }
            case 6:
            {
                cout << "Linked list is: " << endl;
                sll.printSLL();
                break;
            }
            case 7:
            {
                system("clear");
                break;
            }
            case 8:
            {
                cout << "=====================================================================================" << endl;
                break;
            }
            default:
            {
                cout << "Invalid Input.\nTry Again:)" << endl;
                break;
            }
            }
        } while ((int)varForSwitchCase -'0' != 8);
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
    int key1, k1;
    char varForDataTypeSelection;
    do
    {
    Title:
        cout << "\n=====================================================================================" << endl;
        cout << "**************************** Singly Linked list Operations **************************" << endl;
        cout << "=====================================================================================" << endl;
        cout << "\nSelect Data type: " << endl;
        cout << "1.INT     2.DOUBLE    3.FLOAT     4.CHAR     5.Clear Screen     6.Exit" << endl;
        cout << ">";
        cin >> varForDataTypeSelection;
        if (!isdigit(varForDataTypeSelection))
        {
            cout << "Invalid Input" << endl;
            goto Title;
        }
        if (((int)varForDataTypeSelection - '0') == 1)
        {
            int data1;
            cout << "Selected Data type: INT" << endl;
            cout << "Which Operation Do you want to perform?" << endl;
            SLL<int> sll;
            sll.generalSwitchCaseBlock(sll);
        }
        else if (((int)varForDataTypeSelection - '0') == 2)
        {
            double data1;
            cout << "Selected Data type: DOUBLE" << endl;
            cout << "Which Operation Do you want to perform?" << endl;
            SLL<double> sll;
            sll.generalSwitchCaseBlock(sll);
        }
        else if (((int)varForDataTypeSelection - '0') == 3)
        {
            float data1;
            cout << "Selected Data type: FLOAT" << endl;
            cout << "Which Operation Do you want to perform?" << endl;
            SLL<float> sll;
            sll.generalSwitchCaseBlock(sll);
        }
        else if (((int)varForDataTypeSelection - '0') == 4)
        {
            char data1;
            cout << "Selected Data type: CHAR" << endl;
            cout << "Which Operation Do you want to perform?" << endl;
            SLL<char> sll;
            sll.generalSwitchCaseBlock(sll);
        }
        else if (((int)varForDataTypeSelection - '0') == 5)
        {
            system("clear");
        }
        else if (((int)varForDataTypeSelection - '0') == 6)
        {
            cout << "\n\nThanks for Using Program!" << endl;
            cout << "Program Ended\n\n"
                 << endl;
            cout << "=====================================================================================" << endl;
            cout << "=====================================================================================" << endl;
            break;
        }
        else
        {
            cout << "Invalid input!\nTry Again:)" << endl;
        }

    } while (((int)varForDataTypeSelection - '0') != 6);
    return 0;
}