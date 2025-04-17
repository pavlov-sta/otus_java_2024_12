package ru.otus.crm.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role_client")
    @Enumerated(EnumType.STRING)
    private RoleClient roleClient;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    public Client(Long id, String name, String login, String password, RoleClient roleClient) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.roleClient = roleClient;
    }

    public Client(String name, String login, String password, RoleClient roleClient) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.roleClient = roleClient;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;

        if (phones != null) {
            for (Phone phone : phones) {
                phone.setClient(this);
            }
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Client clonedClient = new Client(this.id, this.name, this.login, this.password, this.roleClient);
        if (this.address != null) {
            Address clonedAddress = new Address(this.address.getId(), this.address.getStreet());
            clonedClient.setAddress(clonedAddress);
        }
        if (this.phones != null) {
            clonedClient.setPhones(this.phones.stream()
                    .map(phone -> {
                        Phone clonedPhone = new Phone(phone.getId(), phone.getNumber());
                        clonedPhone.setClient(clonedClient);
                        return clonedPhone;
                    })
                    .collect(Collectors.toList()));
        }
        return clonedClient;
    }

    @Override
    public String toString() {
        return "Client{" + "id="
                + id + ", name='"
                + name + '\'' + ", login='"
                + login + '\'' + ", password='"
                + password + '\'' + ", roleClient="
                + roleClient + ", address="
                + address + ", phones="
                + phones + '}';
    }
}
