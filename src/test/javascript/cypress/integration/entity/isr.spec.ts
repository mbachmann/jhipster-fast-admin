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

describe('Isr e2e test', () => {
  const isrPageUrl = '/isr';
  const isrPageUrlPattern = new RegExp('/isr(\\?.*)?$');
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
    cy.intercept('GET', '/api/isrs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/isrs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/isrs/*').as('deleteEntityRequest');
  });

  it('should load Isrs', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('isr');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Isr').should('exist');
    cy.url().should('match', isrPageUrlPattern);
  });

  it('should load details Isr page', function () {
    cy.visit(isrPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('isr');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', isrPageUrlPattern);
  });

  it('should load create Isr page', () => {
    cy.visit(isrPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Isr');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', isrPageUrlPattern);
  });

  it('should load edit Isr page', function () {
    cy.visit(isrPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Isr');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', isrPageUrlPattern);
  });

  it('should create an instance of Isr', () => {
    cy.visit(isrPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Isr');

    cy.get(`[data-cy="defaultIsr"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultIsr"]`).click().should('be.checked');

    cy.get(`[data-cy="type"]`).select('RIS');

    cy.get(`[data-cy="position"]`).select('FIRST_PAGE');

    cy.get(`[data-cy="name"]`).type('Fantastic').should('have.value', 'Fantastic');

    cy.get(`[data-cy="bankName"]`).type('Public-key Table calculate').should('have.value', 'Public-key Table calculate');

    cy.get(`[data-cy="bankAddress"]`).type('Accounts invoice').should('have.value', 'Accounts invoice');

    cy.get(`[data-cy="recipientName"]`).type('Illinois system index').should('have.value', 'Illinois system index');

    cy.get(`[data-cy="recipientAddition"]`).type('Money portal').should('have.value', 'Money portal');

    cy.get(`[data-cy="recipientStreet"]`).type('turn-key flexibility Fish').should('have.value', 'turn-key flexibility Fish');

    cy.get(`[data-cy="recipientCity"]`).type('deposit up Corporate').should('have.value', 'deposit up Corporate');

    cy.get(`[data-cy="deliveryNumber"]`).type('purple hard').should('have.value', 'purple hard');

    cy.get(`[data-cy="iban"]`).type('MT57IDQG07009677018788V37044976').should('have.value', 'MT57IDQG07009677018788V37044976');

    cy.get(`[data-cy="subscriberNumber"]`).type('Nicaragua').should('have.value', 'Nicaragua');

    cy.get(`[data-cy="leftPrintAdjust"]`).type('34231').should('have.value', '34231');

    cy.get(`[data-cy="topPrintAdjust"]`).type('29452').should('have.value', '29452');

    cy.get(`[data-cy="created"]`).type('2021-08-16T14:30').should('have.value', '2021-08-16T14:30');

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
    cy.url().should('match', isrPageUrlPattern);
  });

  it('should delete last instance of Isr', function () {
    cy.visit(isrPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('isr').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', isrPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
