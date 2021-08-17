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

describe('CatalogUnit e2e test', () => {
  const catalogUnitPageUrl = '/catalog-unit-fa';
  const catalogUnitPageUrlPattern = new RegExp('/catalog-unit-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/catalog-units+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalog-units').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalog-units/*').as('deleteEntityRequest');
  });

  it('should load CatalogUnits', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalog-unit-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogUnit').should('exist');
    cy.url().should('match', catalogUnitPageUrlPattern);
  });

  it('should load details CatalogUnit page', function () {
    cy.visit(catalogUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('catalogUnit');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogUnitPageUrlPattern);
  });

  it('should load create CatalogUnit page', () => {
    cy.visit(catalogUnitPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogUnit');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogUnitPageUrlPattern);
  });

  it('should load edit CatalogUnit page', function () {
    cy.visit(catalogUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogUnit');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogUnitPageUrlPattern);
  });

  it('should create an instance of CatalogUnit', () => {
    cy.visit(catalogUnitPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogUnit');

    cy.get(`[data-cy="remoteId"]`).type('47877').should('have.value', '47877');

    cy.get(`[data-cy="name"]`).type('policy needs-based').should('have.value', 'policy needs-based');

    cy.get(`[data-cy="nameDe"]`).type('primary microchip').should('have.value', 'primary microchip');

    cy.get(`[data-cy="nameEn"]`).type('e-services').should('have.value', 'e-services');

    cy.get(`[data-cy="nameFr"]`).type('Investment HTTP Canyon').should('have.value', 'Investment HTTP Canyon');

    cy.get(`[data-cy="nameIt"]`).type('neural hack capacitor').should('have.value', 'neural hack capacitor');

    cy.get(`[data-cy="scope"]`).select('PRODUCT');

    cy.get(`[data-cy="custom"]`).should('not.be.checked');
    cy.get(`[data-cy="custom"]`).click().should('be.checked');

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
    cy.url().should('match', catalogUnitPageUrlPattern);
  });

  it('should delete last instance of CatalogUnit', function () {
    cy.visit(catalogUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('catalogUnit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogUnitPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
