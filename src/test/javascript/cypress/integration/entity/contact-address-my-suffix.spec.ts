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

describe('ContactAddress e2e test', () => {
  const contactAddressPageUrl = '/contact-address-my-suffix';
  const contactAddressPageUrlPattern = new RegExp('/contact-address-my-suffix(\\?.*)?$');
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
    cy.intercept('GET', '/api/contact-addresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-addresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-addresses/*').as('deleteEntityRequest');
  });

  it('should load ContactAddresses', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-address-my-suffix');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactAddress').should('exist');
    cy.url().should('match', contactAddressPageUrlPattern);
  });

  it('should load details ContactAddress page', function () {
    cy.visit(contactAddressPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contactAddress');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAddressPageUrlPattern);
  });

  it('should load create ContactAddress page', () => {
    cy.visit(contactAddressPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAddress');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAddressPageUrlPattern);
  });

  it('should load edit ContactAddress page', function () {
    cy.visit(contactAddressPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAddress');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAddressPageUrlPattern);
  });

  it('should create an instance of ContactAddress', () => {
    cy.visit(contactAddressPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAddress');

    cy.get(`[data-cy="defaultAddress"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultAddress"]`).click().should('be.checked');

    cy.get(`[data-cy="country"]`).type('Congo').should('have.value', 'Congo');

    cy.get(`[data-cy="street"]`).type('Gusikowski Groves').should('have.value', 'Gusikowski Groves');

    cy.get(`[data-cy="streetNo"]`).type('Extension').should('have.value', 'Extension');

    cy.get(`[data-cy="street2"]`).type('lime Steel Spurs').should('have.value', 'lime Steel Spurs');

    cy.get(`[data-cy="postcode"]`).type('Cambodia').should('have.value', 'Cambodia');

    cy.get(`[data-cy="city"]`).type('Keaganborough').should('have.value', 'Keaganborough');

    cy.get(`[data-cy="hideOnDocuments"]`).should('not.be.checked');
    cy.get(`[data-cy="hideOnDocuments"]`).click().should('be.checked');

    cy.get(`[data-cy="defaultPrepage"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultPrepage"]`).click().should('be.checked');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAddressPageUrlPattern);
  });

  it('should delete last instance of ContactAddress', function () {
    cy.visit(contactAddressPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contactAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactAddressPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
