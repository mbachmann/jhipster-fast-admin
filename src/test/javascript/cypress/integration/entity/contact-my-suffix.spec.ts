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

describe('Contact e2e test', () => {
  const contactPageUrl = '/contact-my-suffix';
  const contactPageUrlPattern = new RegExp('/contact-my-suffix(\\?.*)?$');
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
    cy.intercept('GET', '/api/contacts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contacts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contacts/*').as('deleteEntityRequest');
  });

  it('should load Contacts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-my-suffix');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Contact').should('exist');
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load details Contact page', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contact');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load create Contact page', () => {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load edit Contact page', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should create an instance of Contact', () => {
    cy.visit(contactPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');

    cy.get(`[data-cy="number"]`).type('open-source transparent').should('have.value', 'open-source transparent');

    cy.get(`[data-cy="relation"]`).select('CUSTOMER');

    cy.get(`[data-cy="type"]`).select('PRIVATE');

    cy.get(`[data-cy="gender"]`).select('FEMALE');

    cy.get(`[data-cy="genderSalutationActive"]`).should('not.be.checked');
    cy.get(`[data-cy="genderSalutationActive"]`).click().should('be.checked');

    cy.get(`[data-cy="name"]`).type('navigating').should('have.value', 'navigating');

    cy.get(`[data-cy="nameAddition"]`).type('compress encryption SQL').should('have.value', 'compress encryption SQL');

    cy.get(`[data-cy="salutation"]`).type('Health intuitive').should('have.value', 'Health intuitive');

    cy.get(`[data-cy="phone"]`).type('(995) 782-7065 x48310').should('have.value', '(995) 782-7065 x48310');

    cy.get(`[data-cy="fax"]`).type('Mississippi Angola Bacon').should('have.value', 'Mississippi Angola Bacon');

    cy.get(`[data-cy="email"]`).type('Amir33@yahoo.com').should('have.value', 'Amir33@yahoo.com');

    cy.get(`[data-cy="website"]`).type('Steel Buckinghamshire Greens').should('have.value', 'Steel Buckinghamshire Greens');

    cy.get(`[data-cy="notes"]`).type('solution Designer').should('have.value', 'solution Designer');

    cy.get(`[data-cy="communicationLanguage"]`).type('Berkshire').should('have.value', 'Berkshire');

    cy.get(`[data-cy="communicationChannel"]`).type('B2B Web Bangladesh').should('have.value', 'B2B Web Bangladesh');

    cy.get(`[data-cy="communicationNewsletter"]`).type('panel Associate Shoes').should('have.value', 'panel Associate Shoes');

    cy.get(`[data-cy="currency"]`).type('web-enabled Soap hack').should('have.value', 'web-enabled Soap hack');

    cy.get(`[data-cy="ebillAccountId"]`).type('customized Florida').should('have.value', 'customized Florida');

    cy.get(`[data-cy="vatIdentification"]`).type('Savings ubiquitous Coordinator').should('have.value', 'Savings ubiquitous Coordinator');

    cy.get(`[data-cy="vatRate"]`).type('94690').should('have.value', '94690');

    cy.get(`[data-cy="discountRate"]`).type('76117').should('have.value', '76117');

    cy.get(`[data-cy="discountType"]`).type('Well').should('have.value', 'Well');

    cy.get(`[data-cy="paymentGrace"]`).type('66861').should('have.value', '66861');

    cy.get(`[data-cy="hourlyRate"]`).type('12415').should('have.value', '12415');

    cy.get(`[data-cy="created"]`).type('2021-08-04T08:14').should('have.value', '2021-08-04T08:14');

    cy.get(`[data-cy="mainAddressId"]`).type('47888').should('have.value', '47888');

    cy.setFieldSelectToLastOfEntity('mainAddress');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should delete last instance of Contact', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contact').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
