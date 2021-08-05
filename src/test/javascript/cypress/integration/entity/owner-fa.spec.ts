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

describe('Owner e2e test', () => {
  const ownerPageUrl = '/owner-fa';
  const ownerPageUrlPattern = new RegExp('/owner-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/owners+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/owners').as('postEntityRequest');
    cy.intercept('DELETE', '/api/owners/*').as('deleteEntityRequest');
  });

  it('should load Owners', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('owner-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Owner').should('exist');
    cy.url().should('match', ownerPageUrlPattern);
  });

  it('should load details Owner page', function () {
    cy.visit(ownerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('owner');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', ownerPageUrlPattern);
  });

  it('should load create Owner page', () => {
    cy.visit(ownerPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Owner');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', ownerPageUrlPattern);
  });

  it('should load edit Owner page', function () {
    cy.visit(ownerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Owner');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', ownerPageUrlPattern);
  });

  it('should create an instance of Owner', () => {
    cy.visit(ownerPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Owner');

    cy.get(`[data-cy="remoteId"]`).type('83687').should('have.value', '83687');

    cy.get(`[data-cy="name"]`).type('Beauty Cambridgeshire').should('have.value', 'Beauty Cambridgeshire');

    cy.get(`[data-cy="surname"]`).type('Rupiah').should('have.value', 'Rupiah');

    cy.get(`[data-cy="email"]`).type('Allie.Runte71@gmail.com').should('have.value', 'Allie.Runte71@gmail.com');

    cy.get(`[data-cy="language"]`).select('GERMAN');

    cy.get(`[data-cy="companyName"]`).type('seamless Clothing').should('have.value', 'seamless Clothing');

    cy.get(`[data-cy="companyAddition"]`).type('wireless').should('have.value', 'wireless');

    cy.get(`[data-cy="companyCountry"]`).type('real-time').should('have.value', 'real-time');

    cy.get(`[data-cy="companyStreet"]`).type('auxiliary deposit compress').should('have.value', 'auxiliary deposit compress');

    cy.get(`[data-cy="companyStreetNo"]`).type('Rustic').should('have.value', 'Rustic');

    cy.get(`[data-cy="companyStreet2"]`).type('Shores Bedfordshire Refined').should('have.value', 'Shores Bedfordshire Refined');

    cy.get(`[data-cy="companyPostcode"]`).type('Mouse').should('have.value', 'Mouse');

    cy.get(`[data-cy="companyCity"]`).type('National').should('have.value', 'National');

    cy.get(`[data-cy="companyPhone"]`).type('yellow').should('have.value', 'yellow');

    cy.get(`[data-cy="companyFax"]`).type('Sausages bandwidth Optimization').should('have.value', 'Sausages bandwidth Optimization');

    cy.get(`[data-cy="companyEmail"]`).type('reintermediate').should('have.value', 'reintermediate');

    cy.get(`[data-cy="companyWebsite"]`).type('Handmade Associate interface').should('have.value', 'Handmade Associate interface');

    cy.get(`[data-cy="companyVatId"]`).type('embrace Kids Division').should('have.value', 'embrace Kids Division');

    cy.get(`[data-cy="companyCurrency"]`).select('EUR');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', ownerPageUrlPattern);
  });

  it('should delete last instance of Owner', function () {
    cy.visit(ownerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('owner').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
