describe('Account spec', () => {
  it('should login successfully', () => {
    cy.visit('/login');
    
    cy.intercept('GET', '/api/session', []);
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'test@example.com',
        firstname: 'John',
        lastname: 'Doe',
        admin: true,
      },
    });
  
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);
  
    cy.url().should('include', '/sessions');
  });


  it("should display account's infos", () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'test@example.com',
        lastname: 'Doe',
        firstname: 'John',
        admin: false,
        createdAt: '2024-01-02T00:00:00',
        updatedAt: '2024-01-02T00:00:00',
      },
    }).as('user-infos');

    cy.get('[routerlink="me"]').click();
    cy.url().should('include', '/me');

    cy.wait('@user-infos').then((interception) => {
      const user = interception.response && interception.response.body;
      if (user) {
        cy.get('p:contains("Email:")').should('include.text', user.email);
    
        if (user.admin) {
          cy.get('.my2:contains("You are admin")').should('exist');
        } else {
          cy.get('.my2:contains("You are admin")').should('not.exist');
        }
      }
    });
  });

  it("should delete user's account", () => {

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'test@example.com',
        lastname: 'Doe',
        firstname: 'John',
        admin: false,
        createdAt: '2024-01-02T00:00:00',
        updatedAt: '2024-01-02T00:00:00',
      }
    });

    cy.intercept('GET', '/api/session', []);

    cy.intercept('DELETE', '/api/user/1', { statusCode: 200 });

    cy.url().should('include', '/me');
    cy.get('.mat-warn').click();
    cy.get('.mat-snack-bar-container').should('exist');
    cy.get('[routerlink="login"]').should('exist');
    cy.get('[routerlink="register"]').should('exist');
  });

});