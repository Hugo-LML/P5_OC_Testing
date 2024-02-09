/// <reference types="cypress" />
describe('Login spec', () => {
  it('should login successfully', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});

describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should register successfully', () => {
    // Intercept the registration request
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
    }).as('register');

    // Fill out the registration form
    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);

    // Wait for the registration request to complete
    cy.wait('@register');

    // Assert that the URL includes '/login' after successful registration
    cy.url().should('include', '/login');
  });

  it('should set button state to disabled if email or password are not filled', () => {
    cy.get('input[formControlName=firstName]').type("firstname")
    cy.get('input[formControlName=lastName]').type("lastname")
    cy.get('input[formControlName=email]').clear()
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`)
    cy.get('button[type=submit]').should('be.disabled')

  })

  it('should handle registration error (eg: email already used)', () => {
    // Intercept the registration request with an error response
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: {
        error: 'Internal Server Error',
      },
    }).as('register');

    // Fill out the registration form
    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);

    // Wait for the registration request to complete
    cy.wait('@register');

    // Assert that the error handling in the component is working
    cy.get('.error').should('be.visible');
  });
});

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

describe("Full session's user experience as admin", () => {

  it('should login successfully', () => {
    cy.visit('/login');
    
    cy.intercept('GET', '/api/session', { body: [] });
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

  it('should create a session', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/session', { body: [] });

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2024-01-12T13:36:53",
          updatedAt: "2024-01-12T13:36:53",
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2024-01-12T13:36:53",
          updatedAt: "2024-01-12T13:36:53",
        },
      ],
    });

    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      }],
    });

    cy.get('button[routerLink=create]').click();

    cy.url().should('include', '/sessions/create');
    cy.get('input[formControlName=name]').type("cyTest");
    cy.get('input[formControlName=date]').type("2024-02-04");
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type("cyDscp");
    cy.get('button[type=submit]').click();
    
    cy.url().should('include', '/sessions');
    cy.get('.mat-snack-bar-container').should('exist').contains('Session created !');
    cy.get('.list mat-card').should('be.visible');
  });

  it('should update a session', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2024-01-12T13:36:53",
          updatedAt: "2024-01-12T13:36:53",
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2024-01-12T13:36:53",
          updatedAt: "2024-01-12T13:36:53",
        },
      ],
    });

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      },
    });

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: "cyTestUpdated",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscpUpdated",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: "cyTestUpdated",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscpUpdated",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      }],
    });

    cy.get('.mat-card-actions button[ng-reflect-router-link="update,1"]').click();
    cy.url().should('include', '/sessions/update/1');
    cy.get('input[formControlName=name]').clear().type("cyTestUpdated");
    cy.get('textarea[formControlName=description]').clear().type("cyDscpUpdated");
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
    cy.get('.mat-snack-bar-container').should('exist').contains('Session updated !');
    cy.get('.list mat-card').should('be.visible');
  });

  it('should delete a session', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2024-01-12T13:36:53",
        updatedAt: "2024-01-12T13:36:53",
      },
    });
    
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      },
    });
    
    cy.intercept('DELETE', '/api/session/1', { statusCode: 200 });
    
    cy.intercept('GET', '/api/session', { body: [] });
    
    cy.get('.mat-card-actions button[ng-reflect-router-link="detail,1"]').click();
    cy.url().should('include', '/sessions/detail/1');
    cy.get('button[ng-reflect-color="warn"]').should('be.visible');
    cy.get('button[ng-reflect-color="warn"]').click();
    
    cy.url().should('include', '/sessions');
    cy.get('.mat-snack-bar-container').should('exist').contains('Session deleted !');
  });

});

describe("Full session's user experience as user", () => {

  it('should login successfully', () => {
    cy.visit('/login');
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'test@example.com',
        firstname: 'Jane',
        lastname: 'Doey',
        admin: false,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00",
      }]
    });
  
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);
  
    cy.url().should('include', '/sessions');
  });

  it('should participate to a session', () => {
    cy.url().should('include', '/sessions');
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2024-01-12T13:36:53",
        updatedAt: "2024-01-12T13:36:53",
      },
    });
    
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      },
    });

    cy.intercept('POST', '/api/session/1/participate/1', { body: {} });

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2024-01-12T13:36:53",
        updatedAt: "2024-01-12T13:36:53",
      },
    });

    cy.get('.mat-card-actions button[ng-reflect-router-link="detail,1"]').click();
    cy.url().should('include', '/sessions/detail/1');
    
    cy.get('button[ng-reflect-color="primary"]').should('be.visible');
    cy.get('button[ng-reflect-color="primary"]').click();
    
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [1],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      },
    });

    cy.get('.mat-card-title > div > div > .mat-button-base').click();

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: "cyTest",
        date: "2024-02-04T00:00:00.000+00:00",
        teacher_id: 1,
        description: "cyDscp",
        users: [1],
        createdAt: "2024-02-04T00:00:00.000+00:00",
        updatedAt: "2024-02-04T00:00:00.000+00:00"
      }],
    });

  });

  // it('should unparticipate to a session', () => {
  //   cy.url().should('include', '/sessions');

  //   cy.intercept('GET', '/api/session', {
  //     body: [{
  //       id: 1,
  //       name: "cyTest",
  //       date: "2024-02-04T00:00:00.000+00:00",
  //       teacher_id: 1,
  //       description: "cyDscp",
  //       users: [1],
  //       createdAt: "2024-02-04T00:00:00.000+00:00",
  //       updatedAt: "2024-02-04T00:00:00.000+00:00"
  //     }],
  //   });

  //   cy.intercept('GET', '/api/teacher/1', {
  //     body: {
  //       id: 1,
  //       lastName: "DELAHAYE",
  //       firstName: "Margot",
  //       createdAt: "2024-01-12T13:36:53",
  //       updatedAt: "2024-01-12T13:36:53",
  //     },
  //   });
    
  //   cy.intercept('GET', '/api/session/1', {
  //     body: {
  //       id: 1,
  //       name: "cyTest",
  //       date: "2024-02-04T00:00:00.000+00:00",
  //       teacher_id: 1,
  //       description: "cyDscp",
  //       users: [1],
  //       createdAt: "2024-02-04T00:00:00.000+00:00",
  //       updatedAt: "2024-02-04T00:00:00.000+00:00"
  //     },
  //   });

  //   cy.intercept('DELETE', '/api/session/1/participate/1', { body: {} });

  //   cy.intercept('GET', '/api/teacher/1', {
  //     body: {
  //       id: 1,
  //       lastName: "DELAHAYE",
  //       firstName: "Margot",
  //       createdAt: "2024-01-12T13:36:53",
  //       updatedAt: "2024-01-12T13:36:53",
  //     },
  //   });

  //   cy.get('.mat-card-actions button[ng-reflect-router-link="detail,1"]').click();
  //   cy.url().should('include', '/sessions/detail/1');
    
  //   cy.get('button[ng-reflect-color="warn"]').should('be.visible');
  //   cy.get('button[ng-reflect-color="warn"]').click();
    
  //   cy.intercept('GET', '/api/session/1', {
  //     body: {
  //       id: 1,
  //       name: "cyTest",
  //       date: "2024-02-04T00:00:00.000+00:00",
  //       teacher_id: 1,
  //       description: "cyDscp",
  //       users: [],
  //       createdAt: "2024-02-04T00:00:00.000+00:00",
  //       updatedAt: "2024-02-04T00:00:00.000+00:00"
  //     },
  //   });

  //   cy.get('.mat-card-title > div > div > .mat-button-base').click();

  //   cy.intercept('GET', '/api/session', {
  //     body: [{
  //       id: 1,
  //       name: "cyTest",
  //       date: "2024-02-04T00:00:00.000+00:00",
  //       teacher_id: 1,
  //       description: "cyDscp",
  //       users: [],
  //       createdAt: "2024-02-04T00:00:00.000+00:00",
  //       updatedAt: "2024-02-04T00:00:00.000+00:00"
  //     }],
  //   });
  // });
  
});