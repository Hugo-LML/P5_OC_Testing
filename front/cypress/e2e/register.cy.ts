describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should register successfully', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
    }).as('register');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);

    cy.wait('@register');

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
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: {
        error: 'Internal Server Error',
      },
    }).as('register');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("test@example.com");
    cy.get('input[formControlName=password]').type(`${"test123"}{enter}{enter}`);

    cy.wait('@register');

    cy.get('.error').should('be.visible');
  });
});