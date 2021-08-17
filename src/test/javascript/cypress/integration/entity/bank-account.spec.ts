import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('BankAccount e2e test', () => {
  const bankAccountPageUrl = '/bank-account';
  const bankAccountPageUrlPattern = new RegExp('/bank-account(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bank-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bank-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bank-accounts/*').as('deleteEntityRequest');
  });

  it('should load BankAccounts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank-account');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BankAccount').should('exist');
    cy.url().should('match', bankAccountPageUrlPattern);
  });

  it('should load details BankAccount page', function () {
    cy.visit(bankAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('bankAccount');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', bankAccountPageUrlPattern);
  });

  it('should load create BankAccount page', () => {
    cy.visit(bankAccountPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('BankAccount');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', bankAccountPageUrlPattern);
  });

  it('should load edit BankAccount page', function () {
    cy.visit(bankAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('BankAccount');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', bankAccountPageUrlPattern);
  });

  it('should create an instance of BankAccount', () => {
    cy.visit(bankAccountPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('BankAccount');

    cy.get(`[data-cy="remoteId"]`).type('93084').should('have.value', '93084');

    cy.get(`[data-cy="defaultBankAccount"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultBankAccount"]`).click().should('be.checked');

    cy.get(`[data-cy="description"]`).type('program interface e-commerce').should('have.value', 'program interface e-commerce');

    cy.get(`[data-cy="bankName"]`).type('Internal').should('have.value', 'Internal');

    cy.get(`[data-cy="number"]`).type('Handmade').should('have.value', 'Handmade');

    cy.get(`[data-cy="iban"]`).type('MD9789L13HT595H8668VUSK0').should('have.value', 'MD9789L13HT595H8668VUSK0');

    cy.get(`[data-cy="bic"]`).type('YISOMEB1WET').should('have.value', 'YISOMEB1WET');

    cy.get(`[data-cy="postAccount"]`).type('Outdoors').should('have.value', 'Outdoors');

    cy.get(`[data-cy="autoSync"]`).select('REQUESTED');

    cy.get(`[data-cy="autoSyncDirection"]`).select('OUT');

    cy.get(`[data-cy="currency"]`).select('CVE');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', bankAccountPageUrlPattern);
  });

  it('should delete last instance of BankAccount', function () {
    cy.visit(bankAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('bankAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bankAccountPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
