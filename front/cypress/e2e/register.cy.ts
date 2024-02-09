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